package sa.edu.tuwaiq.planteye.view.identity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sa.edu.tuwaiq.planteye.R

private const val TAG = "ResetPasswordDialog"
class ResetPasswordDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner)
        return inflater.inflate(R.layout.reset_pass_dialog_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close dialog on (X) click
        val close: ImageView = view.findViewById(R.id.close_imageView)
        close.setOnClickListener {
            dismiss()
        }

        // Send reset password email!
        val sentEmail: Button = view.findViewById(R.id.send_email_button)
        val emailEditText: TextInputEditText = view.findViewById(R.id.reset_password_email)

        sentEmail.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty() && email.isNotBlank()) {
                Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Reset password email sent successfully")
                        dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Reset password email has been sent successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong :(",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else
                Toast.makeText(requireContext(), "Please fill in the email field.\nEmail address is required!", Toast.LENGTH_SHORT).show()
        }
    }
}