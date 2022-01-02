package sa.edu.tuwaiq.planteye.view.identity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentProfileBinding
import sa.edu.tuwaiq.planteye.model.collections.User
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_IFO
import java.lang.StringBuilder

private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    //TODO You may remove this later - This for test purposes only (Test login and getting user info)
    private val userCollection = Firebase.firestore.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fullName = sharedPreferences.getStringSet(USER_IFO, null)?.elementAt(1)
        val email = sharedPreferences.getStringSet(USER_IFO, null)?.elementAt(0)
        binding.apply {
            profileFullname.text = "Welcome $fullName"
            profileEmail.text = email
            Log.d(TAG, "user email: $email, name: $fullName")
        }

        // Logout
        binding.profileLogout.setOnClickListener {
            // 1. clear the share pref

            // 2. logout from Firestore
            FirestoreServiceRepository.get().firebaseAuth.signOut()

            // 3. navigate the user to the main fragment again
        }
    }

    /** This function is to get the user information (full name) from the Firestore. It uses real-time update, which observe any changes immediately and
     *  reflect it in the UI (kinda like live data) and no need for update or refresh to see changes in the UI */
//    private fun getUserInfo() {
//        userCollection.whereEqualTo("uid", auth.uid).addSnapshotListener { querySnapshot, error ->
//            // Check for errors
//            error?.let {
//                Log.d(TAG, "Snapshot error: ${error.message}")
//                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
//            }
//
//            // Get collection snapshot
//            querySnapshot?.let {
//                val sb = StringBuilder()
//                for (document in querySnapshot.documents) {
//                    var user = document.toObject<User>()
//                    sb.append(user?.fullName)
//                }
//
//                binding.nameTextView.text = sb.toString()
//            }
//        }
//    }
}