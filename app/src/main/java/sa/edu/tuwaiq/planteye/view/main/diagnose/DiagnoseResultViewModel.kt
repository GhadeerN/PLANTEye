package sa.edu.tuwaiq.planteye.view.main.diagnose

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
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
    val diagnoseHistoryLiveData = MutableLiveData<List<DiagnosesDataModel>>()
    val selectedDiagnoseResult = MutableLiveData<DiagnosesDataModel>()

    var diagnoseHistory = mutableListOf<DiagnosesDataModel>()

    // This variable is to take the taken image and pass it to the API to provide the proper diagnose
    lateinit var image: File
    var encodedImage = ""

    /* This function will send an image to the api to get the diagnose result */
    fun callDiagnoseResult(sickPlantImage: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "path: $encodedImage")
                val response = apiRepo.diagnosePlant(
                    IdentifyDiseaseBody(listOf(sickPlantImage))
                )
                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(
                            TAG,
                            "Diagnose success - ${this.healthAssessment!!.diseases!![0].name}"
                        )
                        diagnoseResultLiveData.postValue(this)

                        // If the response data was a plant & not healthy -> save it in the diagnose result history
                        if (this.isPlant!! && !this.healthAssessment.isHealthy!!)
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

    /* This function will save the plant diagnose info into the DB. We'll use this data later to view
       it in the user plant's diagnoses history */
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

    // This function is to retrieve the plant diagnoses history for a specific user
    fun callDiagnoseHistory(userId: String) {
        try {
            val response = firebaseRepo.getDiagnoseResultsCollection(userId)
            response.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d(TAG, "History error - ${error.message}")
                        diagnoseResultErrorLiveData.postValue(error.message)
                        return
                    }

                    value?.let {
                        for (document in it) {
                            val diagnoseResult = document.toObject(DiagnosesDataModel::class.java)
                            Log.d(TAG, "diagnoseResult - $diagnoseResult")
                            diagnoseHistory.add(diagnoseResult)
                        }

                        diagnoseHistoryLiveData.postValue(diagnoseHistory)
                        diagnoseHistory = mutableListOf()
                    }
                }

            })
        } catch (e: Exception) {
            Log.d(TAG, "History catch - ${e.message}")
            diagnoseResultErrorLiveData.postValue(e.message)
        }
    }

    // This function is to remove a specific diagnose result from the diagnoses history
    fun removeDiagnoseResult(userId: String, diagnoseResult: DiagnosesDataModel) {
        viewModelScope.launch {
            try {
                val response = firebaseRepo.removeDiagnoseResult(userId, diagnoseResult)
                response.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "removeDiagnoseResult fail - ${response.exception?.message}")
                        diagnoseResultErrorLiveData.postValue(response.exception?.message)
                    } else
                        Log.d(TAG, "removeDiagnoseResult success")
                }
            } catch (e: Exception) {
                Log.d(TAG, "removeDiagnoseResult fail - catch: ${e.message}")
                diagnoseResultErrorLiveData.postValue(e.message)
            }
        }
    }
}