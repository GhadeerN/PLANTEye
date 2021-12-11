package sa.edu.tuwaiq.planteye.view.identity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentLoginBinding

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate the user to Sign up page (RegisterFragment)
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Login Functionality
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Login Successfully")
                        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                    } else
                        Log.d(TAG, "Task is not successful")
                }
            } else
                Toast.makeText(requireContext(), "Please fill in the require fields", Toast.LENGTH_SHORT).show()
        }

        // Forget password? functionality
        binding.forgetPasswordTextView.setOnClickListener {
            ResetPasswordDialog().show(requireActivity().supportFragmentManager, "ResetPasswordDialog")
        }
    }
}