package sa.edu.tuwaiq.planteye.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.ActivityMainBinding
import sa.edu.tuwaiq.planteye.repositories.ApiServiceRepository
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import sa.edu.tuwaiq.planteye.util.NavHelper

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // For the action bar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO Move it to splash screen later
        ApiServiceRepository.init()

        //TODO Make sure from M&Saad if this is right to be called here!
        FirestoreServiceRepository.init()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (navController.currentDestination?.label == "PLANTEye") {
            return NavHelper.get().goBack()
        } else {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }
    }
}