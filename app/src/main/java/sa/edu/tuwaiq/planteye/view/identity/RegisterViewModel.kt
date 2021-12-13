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
class RegisterViewModel: ViewModel() {
    private val firestore = FirestoreServiceRepository.get()

    val registerLiveData = MutableLiveData<String>()
    val registerErrorLiveData = MutableLiveData<String>()

    fun saveUser(userId: String, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firestore.saveUser(userId, user)
                response.addOnCompleteListener {
                    if (it.isSuccessful) {
                        registerLiveData.postValue("Success")
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