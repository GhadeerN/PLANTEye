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
import sa.edu.tuwaiq.planteye.databinding.DiagnoseHistoryItemLayoutBinding
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_ID
import sa.edu.tuwaiq.planteye.view.main.diagnose.DiagnoseResultViewModel

private const val TAG = "DiagnoseHistoryAdapter"

class DiagnoseHistoryAdapter(val context: Context, val viewModel: DiagnoseResultViewModel) :
    RecyclerView.Adapter<DiagnoseHistoryAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiagnosesDataModel>() {
        override fun areItemsTheSame(
            oldItem: DiagnosesDataModel,
            newItem: DiagnosesDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DiagnosesDataModel,
            newItem: DiagnosesDataModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<DiagnosesDataModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiagnoseHistoryAdapter.ViewHolder {
        return ViewHolder(
            DiagnoseHistoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: DiagnoseHistoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiagnosesDataModel) {
            // Data setup --------------------------------------------------------------------------
            binding.apply {
                diseaseNameTextView.text = item.healthAssessment!!.diseases!![0].name
                diagnoseDateTextView.text = item.metaData?.date

                Glide.with(context).load(item.images!![0].url)
                    .placeholder(R.drawable.ic_plant_placeholder)
                    .into(diagnoseImageView)
            }

            // Item actions ------------------------------------------------------------------------
            // Display the item details in a dialog. In the item layout click -> open the details page
            itemView.setOnClickListener {
                viewModel.selectedDiagnoseResult.postValue(item)
                Log.d(TAG, "posted the user selected data - $item")
                it.findNavController().navigate(R.id.action_diagnosesFragment_to_historyDetailsFragment)
            }

            val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            // Delete diagnose result from the history
            binding.diagnoseDeleteImageButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.alert_diagnose_title)
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        // Respond to negative button press
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.delete) { dialog, _ ->
                        // Respond to positive button press
                        viewModel.removeDiagnoseResult(sharedPref.getString(USER_ID, "")!!, item)
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}