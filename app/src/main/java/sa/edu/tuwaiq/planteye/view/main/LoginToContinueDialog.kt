package sa.edu.tuwaiq.planteye.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.view.identity.LoginFragment
import sa.edu.tuwaiq.planteye.view.identity.RegisterFragment

private const val TAG = "LoginToContinueDialog"
class LoginToContinueDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner)
        return inflater.inflate(R.layout.login_request_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close the dialog
        val close: ImageView = view.findViewById(R.id.close_login_dialog)
        close.setOnClickListener {
            dismiss()
        }

        // Navigate to login
        val login: Button = view.findViewById(R.id.login_button_dialog)
        login.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginToContinueDialog_to_loginFragment)
        }

        // Go to register page
        val createAccount: TextView = view.findViewById(R.id.signup_login_dialog)
        createAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginToContinueDialog_to_registerFragment)
        }
    }
}