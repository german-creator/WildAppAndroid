package com.ivanilov.wildapp.activities

import android.Manifest.permission.CALL_PHONE
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import com.ivanilov.wildapp.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about_us.*


class AboutUsFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).nav_view.visibility = View.GONE

        bt_back.setOnClickListener {
            (activity as MainActivity).nav_view.visibility = View.VISIBLE
            dismiss()
        }

        ib_call.setOnClickListener {

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:+73822231266")

            if (ContextCompat.checkSelfPermission(
                    (activity as MainActivity),
                    CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(callIntent)
            } else {
                requestPermissions(arrayOf(CALL_PHONE), 1)
            }

        }

        ib_inst.setOnClickListener {
            val uri =
                Uri.parse("http://instagram.com/_u/wild_coffee_")

            val i = Intent(Intent.ACTION_VIEW, uri)

            i.setPackage("com.instagram.android")

            try {
                startActivity(i)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/wild_coffee_")
                    )
                )
            }
        }

        tv_about_us.text = Constants.TEXT_ABOUT_US

    }
}