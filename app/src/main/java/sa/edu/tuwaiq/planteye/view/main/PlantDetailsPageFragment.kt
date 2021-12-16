package sa.edu.tuwaiq.planteye.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentPlantDetailsPageBinding
import sa.edu.tuwaiq.planteye.view.adapters.DetailFragmentAdapter

private const val TAG = "PlantDetailsPageFragment"
class PlantDetailsPageFragment : Fragment() {

    lateinit var binding: FragmentPlantDetailsPageBinding
    val viewModel: SavedPlantsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlantDetailsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
    }

    private fun observer() {
        viewModel.selectedPlantInfo.observe(viewLifecycleOwner, { plant ->
            val suggestion = plant.suggestions!![0]

            binding.savedDetailsFamily.text = suggestion.plantDetails!!.taxonomy!!.family
            binding.savedDetailsKingdom.text = suggestion.plantDetails.taxonomy!!.kingdom
            binding.savedDetailsDescription.text = suggestion.plantDetails.wikiDescription!!.value

            binding.savedDetailsMoreInfoButton.setOnClickListener {
                val url = suggestion.plantDetails.wikiDescription.citation
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        })
    }
}