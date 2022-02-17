package sa.edu.tuwaiq.planteye.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import sa.edu.tuwaiq.planteye.view.main.savedplants.NoteFragment
import sa.edu.tuwaiq.planteye.view.main.savedplants.PlantDetailsPageFragment
import sa.edu.tuwaiq.planteye.view.main.savedplants.PlantInfoFragment

// This adapter is to run the view pager with fragments
class DetailFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        // Number of fragments we have
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Switch between fragments
        return when(position) {
            1 -> NoteFragment()
//            0 -> PlantInfoFragment() //TODO
            0 -> PlantDetailsPageFragment()
            else -> Fragment()
        }
    }
}