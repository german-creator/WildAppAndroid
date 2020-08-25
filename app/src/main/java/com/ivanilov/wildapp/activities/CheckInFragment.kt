package com.ivanilov.wildapp.activities

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import java.util.concurrent.TimeUnit

class CheckInFragment : BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var avtivity: CheckInActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        mCurrentUser = auth.currentUser

        tv_to_login.setText(Constants.CHECK_IN_TEXT_BUTTON)

        tv_action_to_login.setOnClickListener {
            val loginFragment = LoginFragment()
            loginFragment.activity = avtivity
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fr_entering, loginFragment, "checkInFragment")
                .addToBackStack(null)
                .commit()
        }


        bt_check_in.setOnClickListener {

            if (et_name.text.toString() == "") {
                showError(Constants.ERROR_PLEASE_INPUT_YOUR_NAME)
            } else {
                if (et_phone_number.text.toString() == "") {
                    showError(Constants.ERROR_PLEASE_INPUT_YOUR_PHONE_NUMBER)
                } else {

                    val phoneNumber = et_phone_number.text.toString().replace("\\D".toRegex(), "")

                    if (phoneNumber.length < 11) {
                        showError(Constants.ERROR_PHONE_NUMBER_WRONG_FORMAT)
                    } else {

                        val countryCode = "+"
                        val completePhoneNumber = countryCode + phoneNumber


                        progressBar.visibility = View.VISIBLE
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            completePhoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            requireActivity(),
                            mCallback

                        )
                    }
                }

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

                progressBar.visibility = View.INVISIBLE


                val inputCodeDialog = InputCodeDialog()
                val args = Bundle()
                args.putString("verificationId", verificationId)
                inputCodeDialog.setArguments(args)
                inputCodeDialog.checkInFragment = this@CheckInFragment
                inputCodeDialog.show(activity!!.supportFragmentManager, "inputCodeDialog")

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


}