package sa.edu.tuwaiq.planteye.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.Suggestion
import sa.edu.tuwaiq.planteye.model.collections.User

class FirestoreServiceRepository {
    private val database = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    // Collections
    private val userCollection = database.collection("users")

    // Login
    fun login(email: String, password: String)= firebaseAuth.signInWithEmailAndPassword(email,password)

    // Register
    fun register(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    // Insert a user into users collection
    /** This function is to save a user info to the Firestore
     *  @Param user of type User */
    fun saveUser(userId:String,user: User) =userCollection.document(userId).set(user)

    // Get user info

    // Save this plant
    fun savePlant(userId: String, plant: PlantDataModel) = userCollection.document(userId).update("savedPlants", FieldValue.arrayUnion(plant))

    // Get user saved Plants info
    fun savedPlants(userId: String) = userCollection.document(userId)

    // Update the user note
    fun updateNote(userId: String, plant: PlantDataModel) = userCollection.document(userId).update("savedPlants", FieldValue.arrayUnion(plant))

    // Remove the saved plant - cold be used to remove the plant OR prevent the redundancy in the user note update
    fun removePlant(userId: String, plant: PlantDataModel) = userCollection.document(userId).update("savedPlants", FieldValue.arrayRemove(plant))

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