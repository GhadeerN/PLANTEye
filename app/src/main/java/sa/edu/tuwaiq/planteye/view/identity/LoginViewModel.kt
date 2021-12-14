package sa.edu.tuwaiq.planteye.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository

private const val TAG = "LoginViewModel"

class LoginViewModel : ViewModel() {
    private val firestore = FirestoreServiceRepository.get()

    val loginLiveData = MutableLiveData<String>()
    val loginErrorLiveData = MutableLiveData<String>()

    // Login
    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firestore.login(email, password)

                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // TODO post user id to use it in sharedPref
                        loginLiveData.postValue(firestore.firebaseAuth.currentUser!!.uid)
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
}