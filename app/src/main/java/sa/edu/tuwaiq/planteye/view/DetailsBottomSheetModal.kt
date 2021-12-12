package sa.edu.tuwaiq.planteye.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import sa.edu.tuwaiq.planteye.databinding.DetailsButtomSheetModalBinding
import sa.edu.tuwaiq.planteye.databinding.FragmentMainBinding
import sa.edu.tuwaiq.planteye.view.adapters.DetailFragmentAdapter

class DetailsBottomSheetModal: BottomSheetDialogFragment() {

    lateinit var binding: DetailsButtomSheetModalBinding
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var adapter: DetailFragmentAdapter

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
    }
}