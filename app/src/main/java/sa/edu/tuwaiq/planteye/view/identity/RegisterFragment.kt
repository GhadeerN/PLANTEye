package sa.edu.tuwaiq.planteye.view.identity

import android.annotation.SuppressLint
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
import com.google.android.material.snackbar.Snackbar
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
import sa.edu.tuwaiq.planteye.util.RegisterValidation
import sa.edu.tuwaiq.planteye.view.*
import java.lang.Exception

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    private val registerViewModel: RegisterViewModel by activityViewModels()

    //TODO Change it to something cuter :)
    private lateinit var progressDialog: ProgressDialog

    // Shared Preference
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    // Registration form validator
    private val validator = RegisterValidation()

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
            val snackbarView = binding.view
            Log.d(TAG, "inside fun")

            if (checkFields(name, email, password, confirmPassword)) {
                if (password == confirmPassword) {
                    progressDialog.show()
                    Log.d(TAG, "Inside if")
                    registerViewModel.register(User(name, email), password)
                } else
                    binding.outlinedTextFieldRePass.error =
                        "Confirm password does not match password field"
            } else
                Snackbar.make(
                    snackbarView,
                    "Please make sure to fill all the required fields",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(binding.textView).show()
        }
    }

    private fun observer() {
        registerViewModel.registerLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()

                // Change the user login state in the sharePref
                sharedPrefEditor.putBoolean(STATE, true)
                sharedPrefEditor.putString(USER_ID, it)

                sharedPrefEditor.putString(USER_NAME, binding.registerFullName.text.toString().trim())
                sharedPrefEditor.putString(USER_EMAIL, binding.registerEmail.text.toString().trim())
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

    // This function is to check the require fields validity. Returns true if all fields are valid, and false if not
    private fun checkFields(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var state = true
        val emailLayout = binding.outlinedTextFieldEmail
        val fullNameLayout = binding.outlinedTextFieldName
        val passwordLayout = binding.outlinedTextFieldPass
        val confirmPassLayout = binding.outlinedTextFieldRePass

        emailLayout.error = null
        fullNameLayout.error = null
        passwordLayout.error = null
        confirmPassLayout.error = null

        // Get needed string messages from strings.xml resource
        val require = getString(R.string.require)
        val wrongEmailFormat = getString(R.string.wrong_email_format)
        val passwordConditions = getString(R.string.password_format)

        if (fullName.isBlank()) {
            fullNameLayout.error = require
            state = false
        }

        if (email.isBlank()) {
            emailLayout.error = require
            state = false
        } else if (!validator.emailsIsValid(email)) {
            emailLayout.error = wrongEmailFormat
            state = false
        }

        if (password.isBlank()) {
            passwordLayout.error = require
            state = false
        } else if (!validator.passwordIsValid(password)) {
            passwordLayout.error = passwordConditions
            state = false
        }

        if (confirmPassword.isBlank()) {
            confirmPassLayout.error = require
            state = false
        }

        return state
    }
}