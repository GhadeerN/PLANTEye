package sa.edu.tuwaiq.planteye.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.DetailsButtomSheetModalBinding
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.util.NavHelper
import sa.edu.tuwaiq.planteye.view.main.savedplants.PlantInfoViewModel

private const val TAG = "DetailsBottomSheetModal"

class DetailsBottomSheetModal : BottomSheetDialogFragment() {

    lateinit var binding: DetailsButtomSheetModalBinding
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Round shape style (For the card)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsButtomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoLinearLayout.visibility = View.GONE
        binding.progressBar.isVisible = true

        observers()

//        binding.plantImageView.setImageBitmap(viewModel.imageBitmap)

        viewModel.callPlantInfo(viewModel.image)

        // This code is to show expand the bottom sheet on it max height, and add expanded behavior to it
//        val bottomSheet = binding.frameLayout
//        val behavior = BottomSheetBehavior.from(bottomSheet)
//        val layoutParams = bottomSheet.layoutParams
//        val windowHeight = getWindowHeight()
//        Log.d(TAG, behavior.state.toString())
//
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//
//        bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Close the bottom sheet
        binding.closeSheetImageView.setOnClickListener {
            dismiss()
        }
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onResume() {
        super.onResume()
        binding.infoLinearLayout.visibility = View.GONE
    }

    private fun observers() {
        // Plant data observer
        viewModel.plantInfoLiveData.observe(viewLifecycleOwner, {
            /* This will animate the progress bar fading, but the bar will be still in the view group
             * even through we can't see it. To solve this problem, set the progressbar visibility to GONE */
            binding.progressBar.animate().alpha(0f).setDuration(1000)
            binding.progressBar.visibility = View.GONE

            // Set all plant details in the fragment views
            val plantData = it
            if (plantData.isPlant!!) {
                val suggestion = plantData.suggestions!![0]

                binding.detailPlantNameTextView.text = suggestion.plantName
                binding.detailsFamilyTextView.text = suggestion.plantDetails!!.taxonomy!!.family
                binding.detailsKigndomTextView.text = suggestion.plantDetails.taxonomy!!.kingdom
                binding.detailsDescriptionTextView.text =
                    suggestion.plantDetails.wikiDescription!!.value

                val url = suggestion.plantDetails.wikiDescription.citation

                // Implicit intent to direct the user to the plant info web page
                binding.moreInfoButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }

                // Show the info layout
                binding.infoLinearLayout.visibility = View.VISIBLE

                // Note click listener - show the note edit text on user click event
                binding.addNoteTextView.setOnClickListener {
                    binding.addNoteTextView.visibility = View.GONE
                    binding.outlinedTextFieldNote.visibility = View.VISIBLE
                }

                // Save plant info
                binding.savePlantButton.setOnClickListener {
                    // Saved plants object. Why? to add the user note with the plant data and then pass it to the DB repo to save it for this user
                    val savedPlant = SavedPlants(plantData)

                    val userNoteText = binding.userNoteTextInput.text.toString()
                    if (userNoteText.isNotBlank()) { // if the user added a note save it in the model
                        savedPlant.note = userNoteText
                    }
                    Log.d(
                        TAG,
                        "Plant to save: $plantData, userID: ${FirebaseAuth.getInstance().uid!!}"
                    )
                    viewModel.savePlant(FirebaseAuth.getInstance().uid!!, savedPlant)
                }
            } else {
                //TODO replace it with a nice msg not a toast
                Toast.makeText(requireActivity(), "Invalid image", Toast.LENGTH_SHORT).show()
            }
        })

        // Error observer
        viewModel.plantInfoErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.progressBar.animate().alpha(0f).setDuration(1000)
//                binding.errorMsgTextView.visibility = View.VISIBLE
                Toast.makeText(
                    requireActivity(),
                    "Timeout Error: Sorry, please check you intent connection and try again",
                    Toast.LENGTH_LONG
                ).show()
            }
            viewModel.plantInfoErrorLiveData.postValue(null)
        })

        // Save plant observer
        viewModel.savePlantLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(
                    requireActivity(),
                    "The plant is saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.savePlantLiveData.postValue(null)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "on pause Details BottomSheetModal")
        NavHelper.get().hideImage()
    }
}