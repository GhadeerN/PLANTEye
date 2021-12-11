package sa.edu.tuwaiq.planteye.repositories

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreServiceRepository {
    private val database = FirebaseFirestore.getInstance()

    // Collections
    private val userCollection = database.collection("users")

    // Insert a user into users collection

    // Get user info


    // This companion object is to makes our Firebase Service a singleton
    companion object {
        private var instance: FirestoreServiceRepository? = null

        fun init() {
            if (instance == null) {
                instance = FirestoreServiceRepository()
            }
        }

        fun get(): FirestoreServiceRepository {
            return instance ?: throw Exception("Room service repository must be initialized")
        }
    }
}