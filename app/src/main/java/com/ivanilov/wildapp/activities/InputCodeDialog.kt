package com.ivanilov.wildapp.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.*
import com.ivanilov.wildapp.R
import kotlinx.android.synthetic.main.dialog_input_code.view.*
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*

class InputCodeDialog : DialogFragment() {

    private lateinit var verificationId: String
    private lateinit var auth: FirebaseAuth
    lateinit var loginFragment: LoginFragment
    var checkInFragment: CheckInFragment? = null
    private lateinit var dialogView: View


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder =
            AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_input_code, null)
        builder.setView(view)
        dialogView = view

        auth = FirebaseAuth.getInstance()

        verificationId = requireArguments().getString("verificationId", "")

        view.et_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val codeLength = view.et_code.text?.length

                if (codeLength != null && codeLength > 5) {
                    val code = view.et_code.text.toString()
                    val credential = PhoneAuthProvider.getCredential(verificationId, code)
                    signInWithPhoneAuthCredential(credential)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        return builder.create()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (checkInFragment != null){
                        (activity as CheckInActivity).addUserToDataBase(checkInFragment!!.et_name.text.toString())
                    }
                    (activity as CheckInActivity).openMenuAfterSuccessCheckIn()
                    dismiss()

                } else {
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showError("Неверный код, пожалуйста попробуйте еще раз")
                        dialogView.et_code.setText("")
                    }
                }
            }
    }

    private fun showError(textError: String) {
        val builder = AlertDialog.Builder(context)
        val alertView: View =
            layoutInflater.inflate(R.layout.fragment_dialog, null, false)

        alertView.text_dialog.text = textError
        builder.setView(alertView)
        builder.show()
    }


}