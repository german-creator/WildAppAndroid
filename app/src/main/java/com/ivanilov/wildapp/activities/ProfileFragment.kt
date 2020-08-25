package com.ivanilov.wildapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var verificationId: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()


        bt_about_us.setOnClickListener {
            val aboutUsFragment = AboutUsFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, aboutUsFragment, "aboutUsFragment")
                .addToBackStack(null)
                .commit()
        }


        bt_policy.setOnClickListener {
            val userAgreementFragment = UserAgreementFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, userAgreementFragment, "userAgreementFragment")
                .addToBackStack(null)
                .commit()
        }

        bt_logout.setOnClickListener {
            auth.signOut()
            uiUserOffline()
        }

        bt_check_in.setOnClickListener {
            val checkInIntent = Intent(activity, CheckInActivity::class.java)
            checkInIntent.putExtra("fragmentToStart", "CheckIn")
            startActivity(checkInIntent)
        }

        bt_login.setOnClickListener {
            val checkInIntent = Intent(activity, CheckInActivity::class.java)
            checkInIntent.putExtra("fragmentToStart", "Login")
            startActivity(checkInIntent)
        }
        bt_to_change_name.setOnClickListener {
            val checkInIntent = Intent(activity, CheckInActivity::class.java)
            checkInIntent.putExtra("fragmentToStart", "ChangeName")
            checkInIntent.putExtra("CurrentName", tv_name.text.toString())
            startActivity(checkInIntent)
        }


    }

    fun updateUserInfo(name: String, phoneNumber: String) {
        tv_name.text = name
        tv_phone_number.text = ("+7(***)***${phoneNumber.removeRange(0..7)}")
    }

    fun uiUserOffline() {
        tv_welcome_description.visibility = View.VISIBLE
        linearLayoutCompat.visibility = View.VISIBLE

        tv_name.visibility = View.INVISIBLE
        view1.visibility = View.INVISIBLE
        view2.visibility = View.INVISIBLE
        tv_phone_number.visibility = View.INVISIBLE
        bt_logout.visibility = View.INVISIBLE
        bt_to_change_name.visibility = View.INVISIBLE

        var a = rl.layoutParams
        a.height = 300
        rl.layoutParams = a
    }

    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            RealtimeDatabase().getUserFromDatabase(this, auth.currentUser!!.uid, null)
            tv_welcome_description.visibility = View.INVISIBLE
            linearLayoutCompat.visibility = View.INVISIBLE

            tv_name.visibility = View.VISIBLE
            view1.visibility = View.VISIBLE
            view2.visibility = View.VISIBLE
            tv_phone_number.visibility = View.VISIBLE
            bt_to_change_name.visibility = View.VISIBLE
            bt_logout.visibility = View.VISIBLE

            var a = rl.layoutParams
            a.height = 230
            rl.layoutParams = a

        } else {
            uiUserOffline()
        }


    }
}
