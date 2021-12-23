package sa.edu.tuwaiq.planteye.view.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.IdentifyHistoryItemLayoutBinding
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_ID
import sa.edu.tuwaiq.planteye.view.main.SavedPlantsViewModel

private const val TAG = "SavedPlantsAdapter"

class SavedPlantsAdapter(val context: Context, val viewModel: SavedPlantsViewModel) :
    RecyclerView.Adapter<SavedPlantsAdapter.ViewHolder>() {


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SavedPlants>() {
        override fun areItemsTheSame(oldItem: SavedPlants, newItem: SavedPlants): Boolean {
            return oldItem.plant!!.id == newItem.plant!!.id
        }

        override fun areContentsTheSame(oldItem: SavedPlants, newItem: SavedPlants): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<SavedPlants>) {
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
        Log.d(TAG, "Position $position, val: $item")
        holder.bind(item)

        // On card click transform the user to plant details info
        holder.itemView.setOnClickListener {
            Log.d(TAG, "Selected - Position $position, val: $item")
            viewModel.selectedPlantInfo.postValue(item)

            it.findNavController()
                .navigate(R.id.action_savedPlantsFragment2_to_savedPlantDetailsFragment)
        }
        val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        // Delete a plant
        holder.binding.deletePlantImageButton.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Are you sure you want to delete this plant?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Respond to negative button press
                    dialog.dismiss()
                }
                .setPositiveButton("Delete") { dialog, _ ->
                    // Respond to positive button press
                    viewModel.removePlant(sharedPref.getString(USER_ID, "")!!, item.plant!!)
                    dialog.dismiss()
                }
                .show()

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: IdentifyHistoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SavedPlants) {
            binding.apply {
                val suggestion = item.plant!!.suggestions!![0]
                this.itemPlantNameTextView.text = suggestion.plantName
                itemDescriptionTextView.text = suggestion.plantDetails!!.wikiDescription!!.value
                itemFamilyTextView.text = suggestion.plantDetails.taxonomy!!.family
                itemKingdomTextView.text = suggestion.plantDetails.taxonomy.kingdom
                identifyDateTextView.text = item.plant.metaData!!.date

                Glide.with(context).load(item.plant.images!![0].url).into(binding.itemPlantImageView)
            }
        }
    }
}