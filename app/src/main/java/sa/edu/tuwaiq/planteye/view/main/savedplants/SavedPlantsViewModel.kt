package sa.edu.tuwaiq.planteye.view.main.savedplants

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "SavedPlantsViewModel"

class SavedPlantsViewModel : ViewModel() {
    private val firebaseRepo = FirestoreServiceRepository.get()

    var savedPlantsLiveData = MutableLiveData<List<SavedPlants>>()
    var savedPlantsErrorLiveData = MutableLiveData<String>()

    // Delete plant
    var removePlantLiveData = MutableLiveData<String>()

    // This live data is for the itemView that will be selected by the user - to use its data on another fragments
    val selectedPlantInfo = MutableLiveData<SavedPlants>()

    //TODO
    var savedPlants = mutableListOf<SavedPlants>()

    fun callSavedPlants(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //TODO new call for saved plants - using collection
                val response = firebaseRepo.getSavedPlantsCollection(userId)
                response.addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error !=null) {
                            savedPlantsErrorLiveData.postValue(error.message)
                            Log.d(TAG, "DOC SNAPSHOT ERROR: ${error.message}")
                            return
                        }

                        value?.let{
                            // Convert each incoming snapshot to a SavedPlant object
                            for(document in it) {
                                val savedPlantSnap = document.toObject(SavedPlants::class.java)
//                                val savedPlantSnap = document.toObject(SavedPlants::class.java)
                                Log.d(TAG, "SavedPlant Snapshot: $savedPlantSnap")
                                savedPlants.add(savedPlantSnap)
                            }
                            savedPlantsLiveData.postValue(savedPlants)
                            Log.d(TAG, "SavedPlant live data: $savedPlantsLiveData")
                            savedPlants =mutableListOf()
                        }
                    }
                })
//                val response = firebaseRepo.savedPlants(userId)
//                response.addSnapshotListener(object : EventListener<DocumentSnapshot> {
//                    override fun onEvent(
//                        value: DocumentSnapshot?,
//                        error: FirebaseFirestoreException?
//                    ) {
//                        if (error != null) {
//                            savedPlantsErrorLiveData.postValue(error.message)
//                            Log.d(TAG, "DOC SNAPSHOT ERROR: ${error.message}")
//                            return
//                        }
//
//                        // Convert the document snapshot to User object to post it to live data
//                        val snap = value!!.toObject(User::class.java)!!
//                        savedPlantsLiveData.postValue(snap.savedPlants)
//                        Log.d(TAG, "Saved plants: $savedPlantsLiveData")
//                    }
//
//                })

                //------------------------------------------------------------------------------------------
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


    // Delete plant - new delete with savedPlant collection
    fun removePlant(userId: String, plant: PlantDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //TODO I just need the plant id so i may only use it
                val plantToRemove = SavedPlants(plant)
                val response = firebaseRepo.removePlant(userId, plantToRemove)
                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Plant removed successfully")
                        removePlantLiveData.postValue("success")
                    } else {
                        Log.d(TAG, "Remove plant - else: ${response.exception!!.message}")
                        savedPlantsErrorLiveData.postValue(response.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Remove plant - catch: ${e.message}")
                savedPlantsErrorLiveData.postValue(e.message)
            }
        }
    }
//    fun removePlant(userId: String, plant: PlantDataModel) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = firebaseRepo.removePlant(userId, plant)
//                response.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Plant removed successfully")
//                        removePlantLiveData.postValue("success")
//                    } else {
//                        Log.d(TAG, "Remove plant - else: ${response.exception!!.message}")
//                        savedPlantsErrorLiveData.postValue(response.exception!!.message)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "Remove plant - catch: ${e.message}")
//                savedPlantsErrorLiveData.postValue(e.message)
//            }
//        }
//    }
}