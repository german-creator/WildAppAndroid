package com.ivanilov.wildapp.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var avtivity: CheckInActivity
    lateinit var currentName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        et_current_name.setText(currentName)

        bt_change_name.setOnClickListener {
            if (et_new_name.text == null) {

            } else {
                RealtimeDatabase().seUserToDatabase(
                    auth.uid!!,
                    et_new_name.text.toString(),
                    auth.currentUser!!.phoneNumber!!
                )
                requireActivity().finish()
            }


        }

    }


}