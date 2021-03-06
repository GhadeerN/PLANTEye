package sa.edu.tuwaiq.planteye.view.main.savedplants

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentSavedPlantsBinding
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_ID
import sa.edu.tuwaiq.planteye.view.adapters.SavedPlantsAdapter

/*  The fragment where all user saved plants shown */
private const val TAG = "SavedPlantsFragment"

class SavedPlantsFragment : Fragment() {

    lateinit var binding: FragmentSavedPlantsBinding
    private val viewModel: SavedPlantsViewModel by activityViewModels()

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences

    private lateinit var plantsAdapter: SavedPlantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Shared pref initialization
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        binding = FragmentSavedPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()

        plantsAdapter = SavedPlantsAdapter(requireActivity(), viewModel)
        binding.savedPlantsRecycler.adapter = plantsAdapter

        viewModel.callSavedPlants(sharedPref.getString(USER_ID, "")!!)
    }

    private fun observers() {
        viewModel.savedPlantsLiveData.observe(viewLifecycleOwner, {
            binding.savedPlantsProgressBar.animate().alpha(0f).setDuration(1000)
            binding.savedPlantsProgressBar.visibility = View.GONE

            if (it.isEmpty()) {
                binding.empltyListLinearLayout.visibility = View.VISIBLE
            } else {
                binding.empltyListLinearLayout.visibility = View.GONE
            }
            plantsAdapter.submitList(it)
        })

        viewModel.savedPlantsErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.savedPlantsProgressBar.animate().alpha(0f).setDuration(1000)
                binding.savedPlantsProgressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewModel.savedPlantsErrorLiveData.postValue(null)
            }
        })

        // Delete plant
        viewModel.removePlantLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "Plant removed successfully")
                viewModel.removePlantLiveData.postValue(null)
            }
        })
    }
}