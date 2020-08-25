package com.ivanilov.wildapp.models

import android.util.Log
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

object GroupModel : Observable() {

    private var mValueDataListener: ValueEventListener? = null
    private var groupList: ArrayList<Group>? = ArrayList()

    private fun getDataBaseRef(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference.child("Group")
    }

    init {
        if (mValueDataListener != null) {
            getDataBaseRef()?.removeEventListener(mValueDataListener!!)
        }
        mValueDataListener = null
        Log.i("GroupModel", "data init line 24")

        mValueDataListener = object : ValueEventListener {
            override fun onCancelled(datasnapshot: DatabaseError) {
                Log.i("GroupModel", "data update canceled line 28")
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                try {
                    Log.i("GroupModel", "data update line 32")

                    val date: ArrayList<Group> = ArrayList()
                    for (snapshot: DataSnapshot in datasnapshot.children) {
                        try {
                            date.add(Group(snapshot))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    groupList = date
                    Log.i(
                        "GroupModel",
                        "data updated, there are " + groupList!!.size + "entrees in cash"
                    )
                    setChanged()
                    notifyObservers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        getDataBaseRef()?.addValueEventListener(mValueDataListener as ValueEventListener)
    }


}
