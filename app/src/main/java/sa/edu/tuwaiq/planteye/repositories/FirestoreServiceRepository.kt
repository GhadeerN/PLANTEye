package sa.edu.tuwaiq.planteye.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sa.edu.tuwaiq.planteye.model.Suggestion
import sa.edu.tuwaiq.planteye.model.collections.User

class FirestoreServiceRepository {
    private val database = FirebaseFirestore.getInstance()

    // Collections
    private val userCollection = database.collection("users")

    // Insert a user into users collection
    /** This function is to save a user info to the Firestore
     *  @Param user of type User */
    fun saveUser(userId:String,user: User) =userCollection.document(userId).set(user)

    // Get user info

    // Save this plant
    fun savePlant(userId: String, plant: Suggestion) = userCollection.document(userId).update("savedPlants", FieldValue.arrayUnion(plant))


    // This companion object is to makes our Firebase Service a singleton
    companion object {
        private var instance: FirestoreServiceRepository? = null

        fun init() {
            if (instance == null) {
                instance = FirestoreServiceRepository()
            }
        }

        fun get(): FirestoreServiceRepository {
            return instance ?: throw Exception("Firestore service repository must be initialized")
        }
    }
}