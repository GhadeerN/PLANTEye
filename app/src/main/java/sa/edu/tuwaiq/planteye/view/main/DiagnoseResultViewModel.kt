package sa.edu.tuwaiq.planteye.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel
import sa.edu.tuwaiq.planteye.model.body.IdentifyDiseaseBody
import sa.edu.tuwaiq.planteye.repositories.ApiServiceRepository
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.io.File
import java.lang.Exception

private const val TAG = "DiagnoseResultViewModel"

class DiagnoseResultViewModel : ViewModel() {
    // Get access to the API & DB repositories
    private val apiRepo = ApiServiceRepository.get()
    private val firebaseRepo = FirestoreServiceRepository.get()

    // Required Live data to store the results
    val diagnoseResultLiveData = MutableLiveData<DiagnosesDataModel>()
    val diagnoseResultErrorLiveData = MutableLiveData<String>()
    val selectedDiagnoseResult = MutableLiveData<DiagnosesDataModel>()

    // This variable is to take the taken image and pass it to the API to provide the proper diagnose
    lateinit var image: File
    var path = ""

    fun callDiagnoseResult(sickPlantImage: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "path: $path")
                val response = apiRepo.diagnosePlant(
                    IdentifyDiseaseBody(listOf(sickPlantImage))
                )
                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(TAG, "Diagnose success - ${this.healthAssessment.diseases[0].name}")
                        diagnoseResultLiveData.postValue(this)
                        if (this.isPlant && !this.healthAssessment.isHealthy)
                            saveDiagnoseResult(userId, this)
                    }
                } else {
                    Log.d(TAG, "Diagnose fail - else: ${response.message()}")
                    Log.d(TAG, "Diagnose fail - else: ${response.toString()}")
                    diagnoseResultErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception) {
                Log.d(TAG, "Diagnose fail - catch: ${e.message}")
                diagnoseResultErrorLiveData.postValue(e.message)
            }
        }
    }

    // This function will save the plant diagnose info into the DB
    private fun saveDiagnoseResult(userId: String, diagnoseResult: DiagnosesDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.saveDiagnoseResult(userId, diagnoseResult)
                response.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "Save result fail: ${response.exception?.message}")
                        diagnoseResultErrorLiveData.postValue(response.exception?.message)
                    } else
                        Log.d(TAG, "Save result done successfully")
                }
            } catch (e: Exception) {
                Log.d(TAG, "Save result fail - catch: ${e.message}")
                diagnoseResultErrorLiveData.postValue(e.message)
            }
        }
    }
}