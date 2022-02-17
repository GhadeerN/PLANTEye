package sa.edu.tuwaiq.planteye.view.main.diagnose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentHistoryDetailsBinding
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel

private const val TAG = "HistoryDetailsFragment"
class HistoryDetailsFragment : Fragment() {
    lateinit var binding: FragmentHistoryDetailsBinding
    private val diagnoseResultViewModel: DiagnoseResultViewModel by activityViewModels()

    private lateinit var diagnoseResult: DiagnosesDataModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

    }

    private fun observer() {
        diagnoseResultViewModel.selectedDiagnoseResult.observe(viewLifecycleOwner, {
            Log.d(TAG, "result: $it")
//            diagnoseResult = it
            valuesSetup(it)
        })
    }

    private fun valuesSetup(diagnoseResult: DiagnosesDataModel) {
        binding.apply {
            val diseaseResult = diagnoseResult.healthAssessment!!.diseases!![0]
            Log.d(TAG, "disease result: $diseaseResult")
            Glide.with(requireActivity()).load(diagnoseResult.images!![0].url)
                .placeholder(R.drawable.ic_plant_placeholder).into(historyImageView)

            historyDiseaseName.text = diseaseResult.diseaseDetails!!.name
            historyDiscriptionTextView.text = diseaseResult.diseaseDetails.description

            var commonNames = ""
            if (diseaseResult.diseaseDetails.commonNames != null && diseaseResult.diseaseDetails.commonNames?.size!! > 0) {
                diseaseResult.diseaseDetails.commonNames.forEach {
                    commonNames += "- ${it.capitalizeWords()}\n"
                }
                historyCommonNames.text = commonNames
            } else
                historyCommonNames.text = "No common names"

            historyMoreInfoButton.setOnClickListener {
                val url = diseaseResult.diseaseDetails.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    //TODO Repeated code. Also in DiagnoseResultFragment
    private fun String.capitalizeWords(): String =
        split(" ").map { it.capitalize() }.joinToString(" ")
}