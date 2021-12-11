package sa.edu.tuwaiq.planteye.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

import androidx.navigation.ui.setupActionBarWithNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentMainBinding

private const val TAG = "MainFragment"
class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController

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
        val navHostFragment= childFragmentManager.findFragmentById(R.id.fragmentContainerView_main) as NavHostFragment
        navController = navHostFragment.navController

//        setupActionBarWithNavController(navController)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

}