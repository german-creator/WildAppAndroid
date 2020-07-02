package com.ivanilov.wildapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ivanilov.wildapp.model.Group
import com.ivanilov.wildapp.model.GroupModel
import com.ivanilov.wildapp.model.Product

class MenuViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is menu Fragment"
    }

    private val _groupArray = MutableLiveData<MutableList<String>>().apply {

        val groupRef = FirebaseDatabase.getInstance().reference.child("Group")

        var groupFromDatabase: MutableList<String> = mutableListOf()

        groupRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                println(p0!!.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    val group = Group(it)
                    if (group.available){
                        groupFromDatabase.add(group.name)
                    }
                }

            }
        })

        value = groupFromDatabase
        
    }

    private val _productArray = MutableLiveData<MutableList<Product>>().apply {

        val product: Product = Product()

        value = product.getTestProductList()
    }

    fun getGroupArray(): MutableList<String>? {
        return _groupArray.value
    }

    fun getProductArray(): MutableList<Product>? {
        return _productArray.value
    }

}