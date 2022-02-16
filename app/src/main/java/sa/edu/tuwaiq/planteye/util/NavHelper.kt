package sa.edu.tuwaiq.planteye.util

import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController

/* This class is a helper to activate the back button (<-) for the main fragment, science the fragments
*  cannot override the onSupportNavigateUp function to navigate up.
*  - The MainFragment contains the NavHostFragment for our main fragments (articles, diagnoses,..etc.) */
class NavHelper(private val navController: NavController) {

    var imageView: ImageView? = null

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

    /* This function is to hide the user selected image when the bottom sheet is onPause.
    *  It's used in the DetailsBottomSheetModal.kt */
    fun hideImage() {
        if (imageView != null) {
            imageView?.visibility = View.INVISIBLE
        }
    }
}