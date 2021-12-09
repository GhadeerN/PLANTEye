package sa.edu.tuwaiq.planteye.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}