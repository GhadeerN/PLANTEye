package sa.edu.tuwaiq.planteye.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.ActivityMainBinding
import sa.edu.tuwaiq.planteye.repositories.ApiServiceRepository
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    
    lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO Move it to splash screen later
        ApiServiceRepository.init()

        //TODO Make sure from M&Saad if this is right to be called here!
        FirestoreServiceRepository.init()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         /* This line is to hide the shadow for the bottom nav view. The shadow will still appear
          in the xml, but it will disappear when we run the app */
        binding.bottomNavigationView.background = null

        // This line is to disable the placeholder item in the bottom nav view. 1 -> placeholder item index
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
    }
}