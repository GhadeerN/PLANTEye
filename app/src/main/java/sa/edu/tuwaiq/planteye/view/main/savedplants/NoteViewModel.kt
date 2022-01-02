package sa.edu.tuwaiq.planteye.view.main.savedplants

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "NoteViewModel"

class NoteViewModel : ViewModel() {
    private val firebaseRepo = FirestoreServiceRepository.get()

    var noteLiveData = MutableLiveData<String>()
    var noteErrorLiveData = MutableLiveData<String>()

    var removeNoteLiveData = MutableLiveData<String>()

    fun updateNote(userId: String, plant: SavedPlants) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.updateNote(userId, plant)
                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "update done successfully")
                        noteLiveData.postValue(plant.note)
                    } else {
                        Log.d(TAG, "update note - else: ${response.exception?.message}")
                        noteErrorLiveData.postValue(response.exception?.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "update note - catch: ${e.message}")
                noteErrorLiveData.postValue(e.message)
            }
        }
    }
    /* Since the Firestore doesn't support updating array element directly (cause we need the element index and it cannot specify it)
       , we could do the update either by removing (.arrayRemove) and adding (.arrayUnion) the element again in the list -> this is costly operations
       The nice way I found is to update the array element and add it again in one go using .set() :) */
//    fun update(userId: String, oldPlant: PlantDataModel, newPlant: PlantDataModel) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = firebaseRepo.savedPlants(userId)
//                response.addSnapshotListener(object : EventListener<DocumentSnapshot> {
//                    override fun onEvent(
//                        value: DocumentSnapshot?,
//                        error: FirebaseFirestoreException?
//                    ) {
//                        if (error != null) {
//                            noteErrorLiveData.postValue(error.message)
//                            Log.d(TAG, "DOC SNAPSHOT ERROR: ${error.message}")
//                            return
//                        }
//
//                        // First we need to get the entire savedPlant array from DB
//                        val snapOfPlants = value!!.toObject(User::class.java)?.savedPlants
//
//                        snapOfPlants?.let {
//                            // Then in case it was not null - remove the old object from it and add the new one (that contains the user changes)
//                            snapOfPlants.remove(oldPlant)
//                            snapOfPlants.add(newPlant)
//
//                            // Set the saved plant array to the new changes
//                            firebaseRepo.setPlant(userId, snapOfPlants)
//                                .addOnCompleteListener { setTask ->
//                                    Log.d(TAG, "in setPlant")
//                                    if (setTask.isSuccessful) {
//                                        Log.d(TAG, "update done successfully")
//                                        noteLiveData.postValue("success")
//                                    } else {
//                                        Log.d(TAG, "update failed: ${setTask.exception?.message}")
//                                        noteErrorLiveData.postValue(setTask.exception?.message)
//                                    }
//                                }
//                        }
//                    }
//                })
//            } catch (e: Exception) {
//                noteErrorLiveData.postValue(e.message)
//                Log.d(TAG, "update note - catch: ${e.message}")
//            }
//        }
//    }

//    fun updateNote(userId: String, plant: PlantDataModel) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = firebaseRepo.updateNote(userId, plant)
//
//                response.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        noteLiveData.postValue("Success")
//                        Log.d(TAG, "The plant updated successfully")
//                    } else {
//                        noteErrorLiveData.postValue(response.exception?.message)
//                        Log.d(TAG, "Update note - else: ${response.exception?.message}")
//                    }
//                }
//            } catch (e: Exception) {
//                noteErrorLiveData.postValue(e.message)
//                Log.d(TAG, "update note - catch: ${e.message}")
//            }
//        }
//    }

    // Remove the plant to avoid redundancy
//    fun updateNote(userId: String, oldPlant: PlantDataModel, newPlant: PlantDataModel) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = firebaseRepo.removePlant(userId, oldPlant)
//                response.addOnCompleteListener { removeTask ->
//                    if (removeTask.isSuccessful) {
//                        Log.d(TAG, "Note removed successfully")
//                        removeNoteLiveData.postValue("success")
////                        val update = firebaseRepo.savePlant(userId, newPlant)
////                        update.addOnCompleteListener {  addNote ->
////                            if (addNote.isSuccessful) {
////                                noteLiveData.postValue("success")
////                                Log.d(TAG, "Note added successfully")
////                            } else {
////                                Log.d(TAG, "Note not added successfully - else")
////                                noteErrorLiveData.postValue(addNote.exception?.message)
////                            }
////                        }
//                    } else {
//                        Log.d(TAG, "Remove note - else: ${response.exception!!.message}")
//                        noteErrorLiveData.postValue(response.exception!!.message)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "Remove note - catch: ${e.message}")
//                noteErrorLiveData.postValue(e.message)
//            }
//        }
//    }
}