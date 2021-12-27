package sa.edu.tuwaiq.planteye.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentDiagnoseResultBinding
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel
import sa.edu.tuwaiq.planteye.util.ImagePicker
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_ID
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File


private const val TAG = "DiagnoseResultFragment"

class DiagnoseResultFragment : Fragment() {

    lateinit var binding: FragmentDiagnoseResultBinding
    private val diagnoseResultViewModel: DiagnoseResultViewModel by activityViewModels()

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        binding = FragmentDiagnoseResultBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        val imageFile = diagnoseResultViewModel.image
        val encodedImage = base64Encoder(imageFile)
        Log.d(TAG, "file: $imageFile")
        Log.d(TAG, "base64: $encodedImage")

        binding.diseaseDiagnoseLayout.visibility = View.GONE

        diagnoseResultViewModel.callDiagnoseResult(
            diagnoseResultViewModel.path,
            sharedPref.getString(USER_ID, "")!!
        )

        // Convert image to Bitmap to display it in the imageView
        val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        binding.resultDiagnoseImageView.setImageBitmap(imageBitmap)
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        diagnoseResultViewModel.diagnoseResultLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, "Success result - live data")
            binding.resultProgressBar.animate().alpha(0f).setDuration(1000)
            val result = it
            // To handle the incoming result
            /* if the image was not belong to a plant -> show error message, else check if the plant health state
            *  if healthy -> show hurry message to the user, else -> show the diagnose result
            * */
            if (it.isPlant) {
                if (it.healthAssessment.isHealthy) {
                    binding.unproberResultMsg.visibility = View.VISIBLE
                    binding.unproberResultMsg.text =
                        "Hurry 🎉🎉\nYour plant is healthy!\nThank you for using our services"
                } else {
                    binding.unproberResultMsg.visibility = View.GONE
                    setUpTheResult(result)
                }
            } else {
                binding.unproberResultMsg.visibility = View.VISIBLE
                binding.unproberResultMsg.text =
                    "Unfortunately, it seems the picture you took was not for a plant 😆\nPlease try again with a valid image"
            }
        })

        diagnoseResultViewModel.diagnoseResultErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "Error live data - $it")
                binding.resultProgressBar.animate().alpha(0f).setDuration(1000)
                binding.unproberResultMsg.text = "Error:\nSorry we got a small problem. $it\nPlease try again later"
                binding.unproberResultMsg.visibility = View.VISIBLE
                diagnoseResultViewModel.diagnoseResultErrorLiveData.postValue(null)
            }
        })
    }

    private fun setUpTheResult(result: DiagnosesDataModel) {
        binding.diseaseDiagnoseLayout.visibility = View.VISIBLE
        val diseaseResult = result.healthAssessment.diseases[0]
        // Set up the result values
        binding.resultDiseaseName.text = diseaseResult.name

        var commonNames = ""
        diseaseResult.diseaseDetails.commonNames.forEach {
            commonNames += "- $it\n"
        }
        binding.resultCommonNames.text = commonNames

        binding.resultDescribtion.text = diseaseResult.diseaseDetails.description

        binding.resultMoreInfoButton.setOnClickListener {
            val url = diseaseResult.diseaseDetails.url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun base64Encoder(file: File): String {
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}