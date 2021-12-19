package sa.edu.tuwaiq.planteye.view.main

import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sa.edu.tuwaiq.planteye.databinding.FragmentArticleContentBinding
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.images.ImageManager


private const val TAG = "ArticleContentFragment"
class ArticleContentFragment : Fragment() {

    lateinit var binding: FragmentArticleContentBinding
    private val viewModel: ArticlesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        // textView.setText(Html.fromHtml(Html.fromHtml(mHtmlString).toString()));
        // Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observer() {
        viewModel.selectedArticle.observe(viewLifecycleOwner, {
            binding.titleTextView.text = it.title
            Log.d(TAG, "article: $it")
//            binding.contentTextView.text = Html.fromHtml(it.content, Html.FROM_HTML_MODE_LEGACY).toString()
            val str =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                 Html.fromHtml(it.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                 Html.fromHtml(it.content)
            }
            binding.contentTextView.movementMethod = LinkMovementMethod.getInstance()
            binding.contentTextView.text = str
        })
    }
}