package sa.edu.tuwaiq.planteye.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.ArticleItemLayoutBinding
import sa.edu.tuwaiq.planteye.model.collections.Article
import sa.edu.tuwaiq.planteye.view.main.ArticlesViewModel

class ArticlesAdapter(val context: Context, val viewModel: ArticlesViewModel) :
    RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Article>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        return ViewHolder(
            ArticleItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        // Navigate to the article content page
        holder.itemView.setOnClickListener {
            //TODO
//            val plant = mutableListOf<Article>()
//            plant.addAll(differ.currentList)
//            plant.removeAt(position)
//            differ.submitList(plant)

            viewModel.selectedArticle.postValue(item)
            it.findNavController().navigate(R.id.action_articalesFragment_to_articleContentFragment)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: ArticleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.apply {
                articalTitleTextView.text = item.title
                Glide.with(context)
                    .load("https://firebasestorage.googleapis.com/v0/b/planteye-85ae5.appspot.com/o/${item.image}?alt=media&token=${item.token}")
                    .placeholder(R.drawable.ic_plant_placeholder)
                    .into(articalImgImageView)
            }
        }
    }
}