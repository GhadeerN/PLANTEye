package sa.edu.tuwaiq.planteye.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.model.collections.User

class FirestoreServiceRepository {
    private val database = FirebaseFirestore.getInstance()
    private val SAVED_PLANTS = "SavedPlants"
    val firebaseAuth = FirebaseAuth.getInstance()

    // Collections
    private val userCollection = database.collection("users")
    private val articleCollection = database.collection("Articles")
    private val savedPlantsCollection = database.collection("SavedPlants")

    // Login
    fun login(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    // Register
    fun register(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    // Insert a user into users collection
    /** This function is to save a user info to the Firestore
     *  @Param user of type User */
    fun saveUser(userId: String, user: User) = userCollection.document(userId).set(user)

    // Get user info

    // Save this plant - in savedPlant collection, where collection(SavedPlant) -> doc(PlantId) -> (savedPlant, userId) - savedPlantsCollection.document(plant.plant?.id.toString()).set(plant)
    fun savePlant(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).set(plant)

    // Save this plant
//    fun savePlant(userId: String, plant: PlantDataModel) = userCollection.document(userId).update("savedPlants", FieldValue.arrayUnion(plant))

    // Remove the saved plant - cold be used to remove the plant OR prevent the redundancy in the user note update
//    fun removePlant(userId: String, plant: PlantDataModel) = userCollection.document(userId).update("savedPlants", FieldValue.arrayRemove(plant))
    fun removePlant(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).delete()

    // Get user saved Plants info
    // Get the savedPlant collection - to check if the plant id is redundant or not
//    fun savedPlants(userId: String) = userCollection.document(userId)
    //TODO new Saved plant
    fun getSavedPlantsCollection(userId: String) =
        userCollection.document(userId).collection(SAVED_PLANTS)

    // Update the user note - DON'T WORK!!
    fun updateNote(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).update("note", plant.note)


    // Update the user note via removing it and adding the new one in one go using set()
//    fun setPlant(userId: String, plant: List<PlantDataModel>) = userCollection.document(userId).set(
//        mutableMapOf("savedPlants" to plant), SetOptions.merge()
//    )

    // Articles ------------------------------------------------------------------------------------
    fun getArticles() = articleCollection

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