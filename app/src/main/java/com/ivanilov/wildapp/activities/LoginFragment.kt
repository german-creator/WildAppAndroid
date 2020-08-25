package com.ivanilov.wildapp.activities

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import com.ivanilov.wildapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


class LoginFragment : BottomSheetDialogFragment() {

    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var activity: CheckInActivity
    private lateinit var completePhoneNumber: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_to_registration.setText(Constants.SIGN_IN_TEXT_BUTTON)

        tv_action_to_registration.setOnClickListener {
            val checkInFragment =CheckInFragment()
            checkInFragment.avtivity = activity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fr_entering, checkInFragment, "checkInFragment")
                .addToBackStack(null)
                .commit()
        }

        bt_login.setOnClickListener {

            val phoneNumber = tv_phone_number.text.toString().replace("\\D".toRegex(), "")


            if (phoneNumber.length < 11) {
                showError(Constants.ERROR_PHONE_NUMBER_WRONG_FORMAT)
            } else{
                val countryCode = "+"
                completePhoneNumber = countryCode + phoneNumber

                RealtimeDatabase().checkUserExist(completePhoneNumber, this)

                progressBar.visibility = View.VISIBLE

            }
        }

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("CheckIn", "onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("CheckIn", "onVerificationFailed", e)

            }

            override fun onCodeSent(
                verificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, p1)
                Log.w("CheckIn", "onCodeSent: $verificationId")

                val inputCodeDialog = InputCodeDialog()
                val args = Bundle()
                args.putString("verificationId", verificationId)
                inputCodeDialog.setArguments(args)
                inputCodeDialog.loginFragment = this@LoginFragment
                inputCodeDialog.checkInFragment = null

                progressBar.visibility = View.INVISIBLE
                inputCodeDialog.show(activity.supportFragmentManager, "inputCodeDialog")


            }
        }
    }

    fun showError(textError: String) {
        progressBar.visibility = View.INVISIBLE

        val builder = AlertDialog.Builder(context)
        val alertView: View =
            layoutInflater.inflate(R.layout.fragment_dialog, null, false)

        alertView.text_dialog.text = textError
        builder.setView(alertView)
        val alert = builder.show()

        alertView.bt_ok.setOnClickListener {alert.dismiss()}



    }

    fun startUserLogin() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            completePhoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallback
        )
    }


}