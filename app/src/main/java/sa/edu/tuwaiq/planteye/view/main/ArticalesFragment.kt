package sa.edu.tuwaiq.planteye.view.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentArticalesBinding

private const val TAG = "ArticalesFragment"

class ArticalesFragment : Fragment() {
    // View Binding
    lateinit var binding: FragmentArticalesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    // Menu ----------------------------------------------------------------------------------------
    // This fun will listen to the selected item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> findNavController().navigate(R.id.action_articalesFragment_to_profileFragment2)
            R.id.saved_plants -> findNavController().navigate(R.id.action_articalesFragment_to_savedPlantsFragment)
            R.id.log_out -> {
                //TODO Logout from the firebaseAuth & clear the shared pref
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // This will connect the main_menu.xml with the action/tool bar :)
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)

        // Create instance of each item inside the menu
        val profile = menu.findItem(R.id.profile)
        val logout = menu.findItem(R.id.log_out)
        val savedPlants = menu.findItem(R.id.saved_plants)

        //TODO Here check the shared pref login state if the user was not logged in hide the action
        // bar menu items, else show em
    }
}