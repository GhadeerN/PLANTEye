package sa.edu.tuwaiq.planteye.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel
import sa.edu.tuwaiq.planteye.model.collections.SavedPlants
import sa.edu.tuwaiq.planteye.model.collections.User

class FirestoreServiceRepository {
    private val database = FirebaseFirestore.getInstance()
    private val SAVED_PLANTS = "SavedPlants"
    private val DIAGNOSE_RESULTS = "DiagnoseResults"
    val firebaseAuth = FirebaseAuth.getInstance()

    // Collections ---------------------------------------------------------------------------------
    private val userCollection = database.collection("users")
    private val articleCollection = database.collection("Articles")
    private val savedPlantsCollection = database.collection("SavedPlants")

    // Identity ------------------------------------------------------------------------------------
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
    fun getUser(userId: String) = userCollection.document(userId)

    // Plant identification ------------------------------------------------------------------------
    // Save this plant - in savedPlant collection, where collection(SavedPlant) -> doc(PlantId) -> (savedPlant, userId) - savedPlantsCollection.document(plant.plant?.id.toString()).set(plant)
    fun savePlant(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).set(plant)

    // Remove the saved plant
    fun removePlant(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).delete()

    // Get user saved Plants info
    // Get the savedPlant collection - to check if the plant id is redundant or not
    fun getSavedPlantsCollection(userId: String) =
        userCollection.document(userId).collection(SAVED_PLANTS)

    // Update the user note
    fun updateNote(userId: String, plant: SavedPlants) =
        userCollection.document(userId).collection(SAVED_PLANTS).document(
            plant.plant?.id.toString()
        ).update("note", plant.note)


    // Disease diagnoses ---------------------------------------------------------------------------
    // Save plant diagnose result
    fun saveDiagnoseResult(userId: String, plant: DiagnosesDataModel) =
        userCollection.document(userId).collection(DIAGNOSE_RESULTS).document(
            plant.id.toString()
        ).set(plant)

    fun getDiagnoseResultsCollection(userId: String) =
        userCollection.document(userId).collection(DIAGNOSE_RESULTS)

    fun removeDiagnoseResult(userId: String, plant: DiagnosesDataModel) =
        userCollection.document(userId).collection(DIAGNOSE_RESULTS).document(
            plant.id.toString()
        ).delete()

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