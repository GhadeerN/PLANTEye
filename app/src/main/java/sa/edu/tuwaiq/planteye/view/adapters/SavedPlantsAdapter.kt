package sa.edu.tuwaiq.planteye.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.planteye.databinding.IdentifyHistoryItemLayoutBinding
import sa.edu.tuwaiq.planteye.model.PlantDataModel


class SavedPlantsAdapter(val context: Context) :
    RecyclerView.Adapter<SavedPlantsAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlantDataModel>() {
        override fun areItemsTheSame(oldItem: PlantDataModel, newItem: PlantDataModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlantDataModel, newItem: PlantDataModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<PlantDataModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedPlantsAdapter.ViewHolder {
        val binding = IdentifyHistoryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: IdentifyHistoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: PlantDataModel) {
                binding.apply {
                    val suggestion = item.suggestions!![0]
                    this.itemPlantNameTextView.text = suggestion.plantName
                    itemDescriptionTextView.text = suggestion.plantDetails!!.wikiDescription!!.value
                    itemFamilyTextView.text = suggestion.plantDetails.taxonomy!!.family
                    itemKingdomTextView.text = suggestion.plantDetails.taxonomy.kingdom
                    identifyDateTextView.text = item.metaData!!.date

                    Glide.with(context).load(item.images!![0].url).into(binding.iitemPlantImageView)
                    //TODO Menu option functionality for the delete, may change it to (X) :)
                }
            }
    }
}