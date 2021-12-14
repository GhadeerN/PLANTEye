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
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentLoginBinding
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.STATE
import sa.edu.tuwaiq.planteye.view.USER_ID

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()

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
            setTitle("Loading..")
            setCancelable(false)
        }

        // Shared pref initialization
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()

        // Navigate the user to Sign up page (RegisterFragment)
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Login Functionality
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressDialog.show()
                viewModel.login(email, password)
            } else
                Toast.makeText(requireContext(), "Please fill in the require fields", Toast.LENGTH_LONG).show()
        }

        // Forget password? functionality
        binding.forgetPasswordTextView.setOnClickListener {
            ResetPasswordDialog().show(requireActivity().supportFragmentManager, "ResetPasswordDialog")
        }
    }

    // Login live data observers
    private fun observers() {
        viewModel.loginLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()

                // Take the user id from the live data and put it in the shared pref
                sharedPrefEditor.putString(USER_ID, it)
                sharedPrefEditor.putBoolean(STATE, true)
                sharedPrefEditor.commit()

                // If login done successfully direct the user to the main fragments (Articles)
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment2)
                viewModel.loginLiveData.postValue(null)
            }
        })

        viewModel.loginErrorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Incorrect email or password", Toast.LENGTH_LONG).show()
            }
        })
    }
}