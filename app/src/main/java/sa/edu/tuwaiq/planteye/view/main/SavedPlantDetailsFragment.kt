package sa.edu.tuwaiq.planteye.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import sa.edu.tuwaiq.planteye.databinding.FragmentSavedPlantDetailsBinding
import sa.edu.tuwaiq.planteye.view.adapters.DetailFragmentAdapter

/*  This fragment will contains the view pager & layout tabs - so I will initialize the adapter here
*   Also, the plants details view model will be called here to get the plant image and name */
private const val TAG = "SavedPlantDetailsFragment"
class SavedPlantDetailsFragment : Fragment() {

    lateinit var binding: FragmentSavedPlantDetailsBinding

    // For the tabs & view pager
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var adapter: DetailFragmentAdapter

    // To get the user selected item from the SavedPlantsViewModel
    private val viewModel: SavedPlantsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedPlantDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager

        adapter = DetailFragmentAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) {tab, position ->
            when(position) {
                0 -> tab.text = "Plant Info"
                1 -> tab.text = "Note"
            }
        }.attach()
    }

    private fun observer() {
        viewModel.selectedPlantInfo.observe(viewLifecycleOwner, { plant ->
            // Set the plant name and image
            binding.plantTitle.text = plant.plant!!.suggestions!![0].plantName
            Glide.with(this).load(plant.plant.images!![0].url).into(binding.plantImage)
        })
    }
}