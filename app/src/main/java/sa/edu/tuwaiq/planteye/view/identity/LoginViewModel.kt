package sa.edu.tuwaiq.planteye.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.model.collections.User
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository

private const val TAG = "LoginViewModel"

class LoginViewModel : ViewModel() {
    private val firestore = FirestoreServiceRepository.get()

    val loginLiveData = MutableLiveData<String>()
    val loginErrorLiveData = MutableLiveData<String>()
    val userInfoLiveData = MutableLiveData<User>()

    // Login
    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firestore.login(email, password)

                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Post user id to use it in sharedPref
                        val userId = firestore.firebaseAuth.currentUser!!.uid
                        loginLiveData.postValue(userId)
                        getUser(userId)
                        Log.d(TAG, "Register successfully!, response: $response")

                    } else {
                        Log.d(TAG, task.exception!!.message.toString())
                        loginErrorLiveData.postValue(task.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                loginErrorLiveData.postValue(e.message)
            }
        }
    }

    // Get user info
    fun getUser(userId: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val response = firestore.getUser(userId)

                response.addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.d(TAG, "Get user info error: ${error.message}")
                        return@addSnapshotListener
                    }

                    value?.let {
                        val user = value.toObject(User::class.java)!!
                        userInfoLiveData.postValue(user)
                        Log.d(TAG, "User: $user")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error - catch: ${e.message}")
            }
        }
    }
}