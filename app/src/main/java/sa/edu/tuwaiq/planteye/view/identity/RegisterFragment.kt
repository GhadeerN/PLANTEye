package sa.edu.tuwaiq.planteye.view.identity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentRegisterBinding
import sa.edu.tuwaiq.planteye.model.collections.User
import java.lang.Exception

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser

    private val registerViewModel: RegisterViewModel by activityViewModels()

    //TODO Change it to something cuter :)
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.apply {
            setTitle("Loading...")
            setCancelable(false)
        }
        // Inflate the layout
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Navigate the user to Login page (LoginFragment)
        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observer()

        // Register
        Log.d(TAG, "Before click")
        binding.registerButton.setOnClickListener {
            val name = binding.registerFullName.text.toString().trim()
            val email = binding.registerEmail.text.toString().trim()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerConfirmPassword.text.toString()
            Log.d(TAG, "inside fun")

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    progressDialog.show()
                    Log.d(TAG, "Inside if")
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseUser = task.result!!.user!!
                                Log.d(TAG, "User registered successfully!")

                                // Insert user information into the users collection (fullName, email)
                                registerViewModel.saveUser(
                                    firebaseUser.uid,
                                    User(name, firebaseUser.email.toString())
                                )
                                Log.d(TAG, firebaseUser.email.toString())
                            }
                        }
                } else
                    Toast.makeText(
                        requireActivity(),
                        "Your password does not mach confirm password!",
                        Toast.LENGTH_SHORT
                    ).show()
            } else
                Toast.makeText(
                    requireActivity(),
                    "Please make sure to fill all the required fields",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun observer() {
        registerViewModel.registerLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                registerViewModel.registerLiveData.postValue(null)
            }
        })

        registerViewModel.registerErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                registerViewModel.registerErrorLiveData.postValue(null)
            }
        })
    }
}