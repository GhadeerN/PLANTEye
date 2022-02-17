package sa.edu.tuwaiq.planteye.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.collections.User
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception

private const val TAG = "RegisterViewModel"

class RegisterViewModel : ViewModel() {
    private val firestore = FirestoreServiceRepository.get()

    val registerLiveData = MutableLiveData<String>()
    val registerErrorLiveData = MutableLiveData<String>()


    // Register the user
    fun register(user: User, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firestore.register(user.email, password)
                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firestoreUser = task.result!!.user!!

                        // Here save the user info (full name, email, saved_pant []) to the users collection
                        saveUser(firestoreUser.uid, user)
                    } else {
                        registerErrorLiveData.postValue("Error: ${response.exception!!.message}")
                        Log.d(TAG, "Error in user register - else part")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Register Catch: ${e.message}")
                registerErrorLiveData.postValue(e.message)
            }
        }
    }

    /* This function is to save the user info in "users" collection. Why?
       Because the firebase authentication only save the email and password in the auth table
       and we cant save other user required info like name, age, gender ..etc */
    private fun saveUser(userId: String, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firestore.saveUser(userId, user)
                response.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Post the user id here
                        registerLiveData.postValue(firestore.firebaseAuth.currentUser!!.uid)
                        Log.d(TAG, "Register success: $response")
                    } else {
                        Log.d(TAG, "Fail register")
                        registerErrorLiveData.postValue(response.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                registerErrorLiveData.postValue(e.message)
            }
        }
    }
}