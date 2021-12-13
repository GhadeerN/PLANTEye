package sa.edu.tuwaiq.planteye.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentMainBinding
import sa.edu.tuwaiq.planteye.view.main.PlantInfoViewModel
import java.io.File

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private val IMAGE_PICKER = 0
    private val REQUEST_CODE_CP = 1

    // Create instance of the viewModel
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* This line is to hide the shadow for the bottom nav view. The shadow will still appear
          in the xml, but it will disappear when we run the app */
        binding.bottomNavigationView.background = null

        // This line is to disable the placeholder item in the bottom nav view. 1 -> placeholder item index
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        // This code is to enable bottom nav bar navigation trough the fragments
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainerView_main) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        binding.cameraFloatingActionButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "PERMISSION_GRANTED")
                showImagePicker()
            } else
                checkCameraStoragePermission()
        }
    }

    // Here we'll catch the result back from the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult() inside IF")
            // Get the image path - [0] because we'll accept only one image
            val imagePath = Matisse.obtainPathResult(data)[0]

            // Convert image to File
            val imageFile = File(imagePath)

            // Convert image to Bitmap to display it in the imageView
            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            // Set the encoded image to the viewModel for further usage
            viewModel.imageBitmap = imageBitmap

            // Show the image and place it in the imageView
//            binding.plantdetailsImageView.visibility = View.VISIBLE
//            binding.plantdetailsImageView.setImageBitmap(imageBitmap)

            // Show the bottom sheet modal with the result
            val modalBottomSheet = DetailsBottomSheetModal()

            // To force the user to click (X) to close the bottom sheet, instead of clicking outside it or swipe it down to close
            modalBottomSheet.isCancelable = false
            modalBottomSheet.show(requireActivity().supportFragmentManager, "ModalBottomSheet")

            // Encode the image file to Base64, and POST it to the identify API
            val encodedImage = base64Encoder(imageFile)
            viewModel.image = encodedImage

            Log.d(TAG, "Encoded Image: $encodedImage")


        }
    }

    // This function will open an image selector using Matisse library
    private fun showImagePicker() {
        checkCameraStoragePermission()

        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "sa.edu.tuwaiq.planteye"))
            .forResult(IMAGE_PICKER)
    }

    // Since the Plant.id Api only accept base64 string for an image, this function will decode the image file to accomplish that
    private fun base64Encoder(file: File): String {
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    // This function is to check the camera and storage permissions
    // if not granted -> ask for the permissions
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
                REQUEST_CODE_CP
            )
        }
    }
}