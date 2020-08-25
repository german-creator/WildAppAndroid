package com.ivanilov.wildapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import kotlinx.android.synthetic.main.activity_check_in.*

class CheckInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        mCurrentUser = auth.currentUser

        bt_back.setOnClickListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            if (backStackEntryCount == 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }

        if (intent.getStringExtra("fragmentToStart") != null) {
            if (intent.getStringExtra("fragmentToStart") == "CheckIn") {

                val checkInFragment = CheckInFragment()
                checkInFragment.avtivity = this
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fr_entering, checkInFragment, "loginTypeSelectionFragment")
                    .addToBackStack(null)
                    .commit()
            } else {
                if (intent.getStringExtra("fragmentToStart") == "ChangeName") {
                    val changeNameFragment = ChangeNameFragment()
                    changeNameFragment.avtivity = this
                    changeNameFragment.currentName = intent.getStringExtra("CurrentName")!!
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr_entering,
                            changeNameFragment,
                            "changeNameFragment"
                        )
                        .addToBackStack(null)
                        .commit()

                } else {
                    val loginFragment = LoginFragment()
                    loginFragment.activity = this
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fr_entering,
                            loginFragment,
                            "loginTypeSelectionFragment"
                        )
                        .addToBackStack(null)
                        .commit()
                }

            }

        } else {
            val checkInFragment = CheckInFragment()
            checkInFragment.avtivity = this
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_entering, checkInFragment, "loginTypeSelectionFragment")
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onBackPressed() {
        finish()
    }


    fun openMenuAfterSuccessCheckIn() {
        finish()
    }

    fun addUserToDataBase(name: String) {
        RealtimeDatabase().seUserToDatabase(
            auth.currentUser!!.uid,
            name,
            auth.currentUser!!.phoneNumber!!
        )
    }
}