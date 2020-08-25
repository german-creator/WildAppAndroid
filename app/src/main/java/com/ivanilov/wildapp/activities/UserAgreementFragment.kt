package com.ivanilov.wildapp.activities

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about_us.bt_back
import kotlinx.android.synthetic.main.fragment_user_agreement.*


class UserAgreementFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_agreement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).nav_view.visibility = View.GONE



        bt_back.setOnClickListener {
            (activity as MainActivity).nav_view.visibility = View.VISIBLE
            dismiss()
        }

        tv_agreement.text = Constants.TEXT_USER_AGREEMENT
        tv_agreement.setMovementMethod(ScrollingMovementMethod())


    }


}