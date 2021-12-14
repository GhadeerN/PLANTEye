package sa.edu.tuwaiq.planteye.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.ActivityMainBinding
import sa.edu.tuwaiq.planteye.repositories.ApiServiceRepository
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository

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
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}