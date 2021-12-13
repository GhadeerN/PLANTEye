package sa.edu.tuwaiq.planteye.view

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import sa.edu.tuwaiq.planteye.databinding.DetailsButtomSheetModalBinding
import sa.edu.tuwaiq.planteye.databinding.FragmentMainBinding
import sa.edu.tuwaiq.planteye.view.adapters.DetailFragmentAdapter
import sa.edu.tuwaiq.planteye.view.main.PlantInfoViewModel

private const val TAG = "DetailsBottomSheetModal"
class DetailsBottomSheetModal: BottomSheetDialogFragment() {

    lateinit var binding: DetailsButtomSheetModalBinding
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var adapter: DetailFragmentAdapter

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsButtomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager

        adapter = DetailFragmentAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) {tab, position ->
            when(position) {
                1 -> tab.text = "Note"
                0 -> tab.text = "Plant Info"
            }
        }.attach()

        binding.plantImageView.setImageBitmap(viewModel.imageBitmap)

        // This code is to show expand the bottom sheet on it max height, and add expanded behavior to it
        val bottomSheet = binding.frameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        Log.d(TAG, behavior.state.toString())

        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }

        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Close the bottom sheet
        binding.closeSheetImageView.setOnClickListener {
            dismiss()
        }
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}