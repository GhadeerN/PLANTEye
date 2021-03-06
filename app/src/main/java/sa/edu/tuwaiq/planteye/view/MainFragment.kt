package sa.edu.tuwaiq.planteye.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController

import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentMainBinding
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import sa.edu.tuwaiq.planteye.util.ImagePicker
import sa.edu.tuwaiq.planteye.util.NavHelper
import sa.edu.tuwaiq.planteye.view.main.savedplants.PlantInfoViewModel
import java.io.File
import androidx.lifecycle.OnLifecycleEvent


private const val TAG = "MainFragment"
const val FILE_NAME = "User log"
const val STATE = "login_state"
const val USER_ID = "user_id"
const val USER_NAME = "user_name"
const val USER_EMAIL = "user_email"

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private val IMAGE_PICKER = 0

    val modalBottomSheet = DetailsBottomSheetModal()

    // Create instance of the viewModel
    private val plantInfoViewModel: PlantInfoViewModel by activityViewModels()

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor


    // Menu Items
    lateinit var profile: MenuItem
    lateinit var logout: MenuItem
    lateinit var login: MenuItem
    lateinit var savedPlants: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This line is to show the menu items in the action/tool bar
        setHasOptionsMenu(true)

        // Shared pref initialization
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

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

        NavHelper.init(navController)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        (requireActivity() as MainActivity).setupActionBarWithNavController(navController)

        NavHelper.get().imageView = binding.plantdetailsImageView

        // Identify plant button -------------------------------------------------------------------
        binding.cameraFloatingActionButton.setOnClickListener {
            if (sharedPref.getBoolean(STATE, false)) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "PERMISSION_GRANTED")
                    ImagePicker.showImagePicker(requireActivity(), this)
                } else
                    ImagePicker.checkCameraStoragePermission(requireActivity())
            } else {
                findNavController().navigate(R.id.action_mainFragment2_to_loginToContinueDialog)
            }
        }

    }

    // Catch the result back from the image picker -------------------------------------------------
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
            plantInfoViewModel.imageBitmap = imageBitmap

            // Show the bottom sheet modal with the result
            modalBottomSheet.show(requireActivity().supportFragmentManager, "ModalBottomSheet")

            // Show the selected image in the background
            binding.plantdetailsImageView.setImageBitmap(imageBitmap)
            binding.plantdetailsImageView.visibility = View.VISIBLE

            // Encode the image file to Base64, and POST it to the identify API
            val encodedImage = ImagePicker.base64Encoder(imageFile)
            plantInfoViewModel.image = encodedImage
            Log.d(TAG, "Main encoded image: $encodedImage")
        }
    }

    // Menu ----------------------------------------------------------------------------------------
    // This fun will listen to the selected item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> findNavController().navigate(R.id.action_mainFragment2_to_profileFragment)
            R.id.saved_plants -> findNavController().navigate(R.id.action_mainFragment2_to_savedPlantsFragment2)
            R.id.log_out -> {
                // Hide the menu items for logged in user - and show login
                profile.isVisible = false
                logout.isVisible = false
                savedPlants.isVisible = false
                login.isVisible = true

                sharedPrefEditor.putBoolean(STATE, false)
                sharedPrefEditor.putString(USER_ID, "")
                sharedPrefEditor.commit()

                FirestoreServiceRepository.get().firebaseAuth.signOut()
            }
            R.id.login_menu -> findNavController().navigate(R.id.action_mainFragment2_to_loginFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // This will connect the main_menu.xml with the action/tool bar :)
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)

        // Create instance of each item inside the menu
        profile = menu.findItem(R.id.profile)
        logout = menu.findItem(R.id.log_out)
        savedPlants = menu.findItem(R.id.saved_plants)
        login = menu.findItem(R.id.login_menu)

        /* Here check the shared pref login state if the user was not logged in hide the action
           bar menu items, else show em */
        if (!sharedPref.getBoolean(STATE, false)) { // false -> ! false -> true -> hide icons :)
            logout.isVisible = false
            profile.isVisible = false
            savedPlants.isVisible = false
            login.isVisible = true
        } else {
            login.isVisible = false
        }
    }

}