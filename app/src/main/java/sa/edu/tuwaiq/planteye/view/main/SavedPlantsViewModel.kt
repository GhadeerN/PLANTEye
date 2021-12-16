package sa.edu.tuwaiq.planteye.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.collections.User
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "SavedPlantsViewModel"
class SavedPlantsViewModel: ViewModel() {
    private val firebaseRepo = FirestoreServiceRepository.get()

    var savedPlantsLiveData = MutableLiveData<List<PlantDataModel>>()
    var savedPlantsErrorLiveData = MutableLiveData<String>()
    val selectedPlantInfo = MutableLiveData<PlantDataModel>()

    fun callSavedPlants(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.savedPlants(userId)
                response.addSnapshotListener(object : EventListener<DocumentSnapshot> {
                    override fun onEvent(
                        value: DocumentSnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            savedPlantsErrorLiveData.postValue(error.message)
                            Log.d(TAG, "DOC SNAPSHOT ERROR: ${error.message}")
                            return
                        }

                        val snap = value!!.toObject(User::class.java)!!
                        savedPlantsLiveData.postValue(snap.savedPlants)
                        Log.d(TAG, "Saved plants: $savedPlantsLiveData")
                    }

                })
//                response.addSnapshotListener(object : EventListener<QuerySnapshot> {
//                    override fun onEvent(
//                        value: QuerySnapshot?,
//                        error: FirebaseFirestoreException?
//                    ) {
//                        if (error != null) {
//                            Log.d(TAG, "SNAPSHOT ERROR: ${error.message}")
//                            return
//                        }
//
//                        for (doc : DocumentChange in value?.documentChanges!!) {
//                            if (doc.type == DocumentChange.Type.ADDED) {
//                                val userDoc = doc.document.toObject(User::class.java)
//                                savedPlantsLiveData.postValue(userDoc.savedPlants)
//                            }
//                        }
//                    }
//
//                })
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                savedPlantsErrorLiveData.postValue(e.message)
            }
        }
    }
}