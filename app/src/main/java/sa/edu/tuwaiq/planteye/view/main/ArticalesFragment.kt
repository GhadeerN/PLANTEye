package sa.edu.tuwaiq.planteye.view.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentArticalesBinding
import sa.edu.tuwaiq.planteye.view.adapters.ArticlesAdapter

private const val TAG = "ArticalesFragment"

class ArticalesFragment : Fragment() {
    // View Binding
    lateinit var binding: FragmentArticalesBinding

    private val viewModel: ArticlesViewModel by activityViewModels()
    private lateinit var articleAdapter: ArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        viewModel.callArticles()

        articleAdapter = ArticlesAdapter(requireActivity(), viewModel)
        binding.articlesRecyclerView.adapter = articleAdapter
    }

    private fun observers() {
        viewModel.articlesLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "Article: $it")
                articleAdapter.submitList(it)
            }
        })

        viewModel.articlesErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.articlesErrorLiveData.postValue(null)
            }
        })
    }
}