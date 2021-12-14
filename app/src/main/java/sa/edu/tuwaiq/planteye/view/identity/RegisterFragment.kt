package sa.edu.tuwaiq.planteye.view.identity

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
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
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.STATE
import sa.edu.tuwaiq.planteye.view.USER_ID
import java.lang.Exception

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser

    private val registerViewModel: RegisterViewModel by activityViewModels()

    //TODO Change it to something cuter :)
    private lateinit var progressDialog: ProgressDialog

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.apply {
            setTitle("Loading...")
            setCancelable(false)
        }

        // Shared pref initialization
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

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
                    registerViewModel.register(User(name, email), password)
                } else
                    Toast.makeText(
                        requireActivity(),
                        "Your password does not mach confirm password!",
                        Toast.LENGTH_LONG
                    ).show()
            } else
                Toast.makeText(
                    requireActivity(),
                    "Please make sure to fill all the required fields",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    private fun observer() {
        registerViewModel.registerLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()

                // Change the user login state in the sharePref
                sharedPrefEditor.putBoolean(STATE, true)
                sharedPrefEditor.putString(USER_ID, it)
                sharedPrefEditor.commit()

                findNavController().navigate(R.id.action_registerFragment_to_mainFragment2)
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