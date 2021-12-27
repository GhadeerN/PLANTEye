package sa.edu.tuwaiq.planteye.view.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentDiagnosesBinding
import sa.edu.tuwaiq.planteye.util.IMAGE_PICKER
import sa.edu.tuwaiq.planteye.util.ImagePicker
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.STATE
import sa.edu.tuwaiq.planteye.view.USER_ID
import java.io.ByteArrayOutputStream
import java.io.File

private const val TAG = "DiagnosesFragment"

class DiagnosesFragment : Fragment() {
    lateinit var binding: FragmentDiagnosesBinding
    private val diagnoseResultViewModel: DiagnoseResultViewModel by activityViewModels()

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        binding = FragmentDiagnosesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.diagnoseScanButton.setOnClickListener {
            // Check first if the user logged in -> if T, open the imagePicker. Else, Ask em to log in
            if (sharedPref.getBoolean(STATE, false)) {
//                ImagePicker.showImagePicker(requireActivity(), this)
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "PERMISSION_GRANTED")
                    //TODO use the ImagePicker object instead
//                    ImagePicker.showImagePicker(requireActivity(), this)
                    showImagePicker()
                } else
                    checkCameraStoragePermission()
            } else
            //TODO add the LoginToContinue dialog
                Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult() inside IF")
            // Get the image path - [0] because we'll accept only one image
            val imagePath = Matisse.obtainPathResult(data)[0]
            val image = Matisse.obtainResult(data)[0]

            // Convert image to File
            val imageFile = File(imagePath)
            Log.d(TAG, "file: $imageFile")

            val encodedImage = base64Encoder(imageFile)
            val uriEncode = encoder2(image)
            Log.d(TAG, "encoded image: $encodedImage")
            Log.d(TAG, "uri encode image: $uriEncode")

            // Save necessary data in DiagnoseResultViewModel to use it later in the result page
            diagnoseResultViewModel.image = imageFile
            diagnoseResultViewModel.path = encodedImage

            findNavController().navigate(R.id.action_diagnosesFragment_to_diagnoseResultFragment)
        }
    }

    private fun base64Encoder(file: File): String {
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun checkCameraStoragePermission() {
        if (
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "PERMISSION_DENIED")
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                9
            )
        }
    }

    private fun showImagePicker() {
        checkCameraStoragePermission()

        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "sa.edu.tuwaiq.planteye"))
            .forResult(11)
    }

    private fun encoder2(imageUri: Uri): String {
        val input = requireActivity().contentResolver.openInputStream(imageUri)
        //val bm = BitmapFactory.decodeResource(resources, R.drawable.test)
        val image = BitmapFactory.decodeStream(input, null, null)
        //encode image to base64 string
        val baos = ByteArrayOutputStream()
        //bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()

        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)
        //return Base64.getEncoder().encodeToString(imageBytes) // Not Worked, too.
    }

//    fun observer() {
//        diagnoseResultViewModel.diagnoseResultLiveData.observe(viewLifecycleOwner, {
//            Log.d(TAG, it.toString())
//            Toast.makeText(requireContext(), "Hurry it workss", Toast.LENGTH_SHORT).show()
//        })
//    }
}