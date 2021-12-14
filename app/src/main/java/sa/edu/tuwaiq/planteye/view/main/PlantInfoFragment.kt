package sa.edu.tuwaiq.planteye.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentPlantInfoBinding

private const val TAG = "PlantInfoFragment"

class PlantInfoFragment : Fragment() {

    private lateinit var binding: FragmentPlantInfoBinding
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()

        viewModel.callPlantInfo(viewModel.image)

    }

    private fun observers() {
        // Plant data observer
        viewModel.plantInfoLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.animate().alpha(0f).setDuration(1000)

            // Set all plant details in the fragment views
            val plantData = it
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

            // Save plant info
            binding.savePlantButton.setOnClickListener {
                viewModel.savePlant(FirebaseAuth.getInstance().uid!!, plantData)
            }
        })

        // Save plant observer
        viewModel.savePlantLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireActivity(), "The plant is saved successfully!", Toast.LENGTH_SHORT).show()
                viewModel.savePlantLiveData.postValue(null)
                return@let
            }
            Toast.makeText(requireContext(), "The plant is already saved!\n Check your bookmark list ❤", Toast.LENGTH_SHORT).show()
        })

        // Error observer
        viewModel.plantInfoErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.progressBar.animate().alpha(0f).setDuration(1000)
                binding.errorMsgTextView.visibility = View.VISIBLE
                Toast.makeText(
                    requireActivity(),
                    "Timeout Error: Sorry, please check you intent connection and try again",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.plantInfoErrorLiveData.postValue(null)
            }
        })
    }
}