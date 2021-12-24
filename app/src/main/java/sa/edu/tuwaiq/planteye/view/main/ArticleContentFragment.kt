package sa.edu.tuwaiq.planteye.view.main

import android.content.Context
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
import androidx.activity.OnBackPressedCallback
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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observer() {
        viewModel.selectedArticle.observe(viewLifecycleOwner, {
            // Web view setup
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "url: ${it.url}")
                creteWebView(it.url)
            }
//            binding.titleTextView.text = it.title
//            Log.d(TAG, "article: $it")
////            binding.contentTextView.text = Html.fromHtml(it.content, Html.FROM_HTML_MODE_LEGACY).toString()
//            val str =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                 Html.fromHtml(it.content, Html.FROM_HTML_MODE_LEGACY)
//            } else {
//                @Suppress("DEPRECATION")
//                 Html.fromHtml(it.content)
//            }
//            binding.contentTextView.movementMethod = LinkMovementMethod.getInstance()
//            binding.contentTextView.text = str
        })
    }

    /* This function is for the web view setup - it takes the webpage url as an argument and load it
       in the webView component */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun creteWebView(url: String) {
        binding.webView.apply {
            loadUrl(url)
//            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
    }

    //TODO Not sure if its working
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    // write your code which you want
                    if (binding.webView.canGoBack()) binding.webView.goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}