package sa.edu.tuwaiq.planteye.util

import androidx.navigation.NavController

/* This class is a helper to activate the back button (<-) for the main fragment, science the fragments
*  cannot override the onSupportNavigateUp function to navigate up.
*  - The MainFragment contains the NavHostFragment for our main fragments (articles, diagnoses,..etc.) */
class NavHelper(private val navController: NavController) {

    companion object {
        var INSTANCE: NavHelper? = null

        fun init(navController: NavController) {
            INSTANCE = NavHelper(navController)
        }

        fun get(): NavHelper {
            return INSTANCE ?: throw Exception("NavHelper must be initialized")
        }
    }

    fun goBack(): Boolean {
        return navController.navigateUp()
    }
}