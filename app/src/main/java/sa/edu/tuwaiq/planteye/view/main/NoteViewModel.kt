package sa.edu.tuwaiq.planteye.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "NoteViewModel"
class NoteViewModel: ViewModel() {
    private val firebaseRepo = FirestoreServiceRepository.get()

    var noteLiveData = MutableLiveData<String>()
    var noteErrorLiveData = MutableLiveData<String>()

    fun updateNote(userId: String, plant: PlantDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.updateNote(userId, plant)

                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        noteLiveData.postValue("Success")
                        Log.d(TAG, "The plant updated successfully")
                    } else {
                        noteErrorLiveData.postValue(response.exception?.message)
                        Log.d(TAG, "Update note - else: ${response.exception?.message}")
                    }
                }
            } catch (e: Exception) {
                noteErrorLiveData.postValue(e.message)
                Log.d(TAG, "update note - catch: ${e.message}")
            }
        }
    }
}