package sa.edu.tuwaiq.planteye.view.main

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.Suggestion
import sa.edu.tuwaiq.planteye.model.body.IdentifyPlantBody
import sa.edu.tuwaiq.planteye.repositories.ApiServiceRepository
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "PlantInfoViewModel"
class PlantInfoViewModel: ViewModel() {
    private val apiRepo = ApiServiceRepository.get()
    private val firebaseRepo = FirestoreServiceRepository.get()

    val plantInfoLiveData = MutableLiveData<PlantDataModel>()
    val savePlantLiveData = MutableLiveData<String>()
    val plantInfoErrorLiveData = MutableLiveData<String>()

    var image = ""
    lateinit var imageBitmap: Bitmap

    fun callPlantInfo(image: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepo.identifyPlant(
                    IdentifyPlantBody(
                        listOf(image)
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(TAG, "plantCall: $this")
                        plantInfoLiveData.postValue(this)
                    }
                } else {
                    Log.d(TAG, "plantCall else: ${response.message()}")
                    plantInfoErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception) {
                Log.d(TAG, "plantCall catch: ${e}")
                plantInfoErrorLiveData.postValue(e.message)
            }
        }
    }


    // Save plant to the user data
    fun savePlant(userId: String, plant: PlantDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.savePlant(userId, plant)
                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Plant data saved successfully!")
                        savePlantLiveData.postValue("Success")
                    } else {
                        Log.d(TAG, "Plant data NOT saved - ELSE")
                        plantInfoErrorLiveData.postValue("error")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: Plant data NOT saved -> ${e.message}")
                plantInfoErrorLiveData.postValue(e.message)
            }
        }
    }
}