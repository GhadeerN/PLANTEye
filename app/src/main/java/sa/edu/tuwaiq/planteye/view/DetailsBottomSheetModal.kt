package sa.edu.tuwaiq.planteye.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.DetailsButtomSheetModalBinding
import sa.edu.tuwaiq.planteye.model.Suggestion
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.util.NavHelper
import sa.edu.tuwaiq.planteye.view.main.savedplants.PlantInfoViewModel

private const val TAG = "DetailsBottomSheetModal"

class DetailsBottomSheetModal : BottomSheetDialogFragment() {

    lateinit var binding: DetailsButtomSheetModalBinding
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Round shape style (For the bottom sheet)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsButtomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()

        viewModel.callPlantInfo(viewModel.image)

        // Close the bottom sheet
        binding.closeSheetImageView.setOnClickListener {
            dismiss()
        }
    }

    private fun observers() {
        Log.d(
            TAG,
            "info:${binding.infoLinearLayout.isVisible}, errorMsg: ${binding.identifyErrorMsg.isVisible}, progress bar: ${binding.progressBar.isVisible}"
        )
        // Plant data observer
        viewModel.plantInfoLiveData.observe(viewLifecycleOwner, {
            /* This will animate the progress bar fading, but the bar will be still in the view group
             * even through we can't see it. To solve this problem, set the progressbar visibility to GONE */
            binding.progressBar.animate().alpha(0f).setDuration(1000)
            binding.progressBar.visibility = View.GONE

            // Set all plant details in the fragment views
            val plantData = it
            if (plantData.isPlant!!) {
                binding.identifyErrorMsg.visibility = View.GONE
                val suggestion = plantData.suggestions!![0]

                fillPlantInfo(suggestion)

                // Show the info layout
                binding.infoLinearLayout.visibility = View.VISIBLE

                val url = suggestion.plantDetails!!.wikiDescription!!.citation

                // Implicit intent to direct the user to the plant info web page
                binding.moreInfoButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }

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
                binding.infoLinearLayout.visibility = View.GONE
                binding.identifyErrorMsg.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.not_plant_msg)
                }
            }
        })

        // Error observer
        viewModel.plantInfoErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.progressBar.animate().alpha(0f).setDuration(1000)
                binding.progressBar.visibility = View.GONE
//                binding.errorMsgTextView.visibility = View.VISIBLE
                Toast.makeText(
                    requireActivity(),
                    "Timeout Error: Sorry, please check you intent connection and try again later",
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

    // Handling the result layout content ----------------------------------------------------------
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "on pause Details BottomSheetModal")
        this.dismiss()
    }

    // The onPause will help in hiding the user selected image when the bottom sheet is dismissed
    override fun onDestroy() {
        super.onDestroy()
        NavHelper.get().hideImage()
    }

    override fun onResume() {
        super.onResume()
        // To hide the bottom sheet info layout or the error msg, and show the progress bar
        binding.progressBar.isVisible = true
        binding.infoLinearLayout.isVisible = false
        binding.identifyErrorMsg.isVisible = false
    }

    // Fill the result layout content --------------------------------------------------------------
    // The function fills the plant information into the views (components)
    private fun fillPlantInfo(suggestion: Suggestion) {
        binding.detailPlantNameTextView.text = suggestion.plantName
        binding.detailsFamilyTextView.text = suggestion.plantDetails!!.taxonomy!!.family
        binding.detailsKigndomTextView.text = suggestion.plantDetails.taxonomy!!.kingdom
        binding.detailsDescriptionTextView.text =
            suggestion.plantDetails.wikiDescription!!.value
    }
}