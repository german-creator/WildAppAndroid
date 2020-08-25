package com.ivanilov.wildapp.firebase

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ivanilov.wildapp.activities.LoginFragment
import com.ivanilov.wildapp.activities.MenuFragment
import com.ivanilov.wildapp.activities.OrderFragment
import com.ivanilov.wildapp.activities.ProfileFragment
import com.ivanilov.wildapp.models.Group
import com.ivanilov.wildapp.models.Order
import com.ivanilov.wildapp.models.Product
import com.ivanilov.wildapp.utils.Constants
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RealtimeDatabase {

    val reference = FirebaseDatabase.getInstance().reference

    fun getGroupList(fragment: MenuFragment) {

        var groupFromDatabase: ArrayList<String> = ArrayList()

        reference.child(Constants.GROUP_TABLE)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println(p0.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    children.forEach {
                        val group = Group(it)
                        if (group.available) {
                            groupFromDatabase.add(group.name)
                        }
                    }
                    fragment.updateGroupAdapter(groupFromDatabase)
                }
            })
    }


    fun getProductList(fragment: MenuFragment) {

        var productList: ArrayList<Product> = ArrayList()

        reference.child(Constants.PRODUCT_TABLE)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println(p0.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    children.forEach {
                        val product = Product(it)
                        if (product.available) {
                            productList.add(product)
                        }
                    }
                    fragment.updateProductAdapter(productList)

                }
            })


    }

    fun seUserToDatabase(uid: String, name: String, phoneNumber: String) {
        var userToDatabase = mapOf("name" to name, "phoneNumber" to phoneNumber)
        reference.child(Constants.USER_TABLE).child(uid).setValue(userToDatabase)
    }

    fun getUserFromDatabase(fragment: ProfileFragment?, uid: String, productToOrder: Map<String, Any>?) {
        reference.child(Constants.USER_TABLE).child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println(p0.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    @Suppress("UNCHECKED_CAST")
                    val value: HashMap<String, Any> = snapshot.value as HashMap<String, Any>

                    val name = value["name"] as String
                    val phoneNumber = value["phoneNumber"] as String

                    if (fragment != null){
                        fragment.updateUserInfo(name, phoneNumber)
                    } else {
                        this@RealtimeDatabase.push(productToOrder!!, name, phoneNumber)
                    }
                }
            })
    }

    fun checkUserExist(phoneNumber: String, fragment: LoginFragment) {

        reference.child(Constants.USER_TABLE)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println(p0.message)
                }


                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children

                    var newUser = true

                    children.forEach { it ->
                        @Suppress("UNCHECKED_CAST")
                        val value: HashMap<String, Any> = it.value as HashMap<String, Any>
                        val phoneNumberSnapshot = value["phoneNumber"] as String
                        if (phoneNumberSnapshot == phoneNumber){
                            newUser = false
                            return@forEach
                        }
                    }

                    if (!newUser) {
                        fragment.startUserLogin()
                    } else {
                        fragment.showError(Constants.ERROR_USER_DO_NOT_EXIST)
                    }




                }
            })
    }

    fun push (productToOrder: Map<String, Any>, name: String, phoneNumber: String){

        var order = mutableMapOf<String, Any>()
        order.putAll(productToOrder)
        order.put("userName", name)
        order.put("userPhone", phoneNumber)

        val orderId: String = reference.child(Constants.ORDER_TABLE).push().getKey()!!

        reference.child(Constants.ORDER_TABLE).child(orderId).setValue(order)


    }

    fun pushOrderToDatabase(order: Order) {

        var productArray: ArrayList<String> = ArrayList()

        order.products.forEach { i ->

            var add0: String = ""
            var add1: String = ""
            var add2: String = ""
            var add3: String = ""

            when (i.add.size) {
                0 -> return
                1 -> {
                    if (i.selectedAdd[0] != 0) {
                        add0 = i.add[0].option[i.selectedAdd[0]]
                    }
                }


                2 -> {
                    if (i.selectedAdd[0] != 0) {
                        add0 = i.add[0].option[i.selectedAdd[0]]
                    }
                    if (i.selectedAdd[1] != 0) {
                        add1 = i.add[1].option[i.selectedAdd[1]]
                    }
                }

                3 -> {

                    if (i.selectedAdd[0] != 0) {
                        add0 = i.add[0].option[i.selectedAdd[0]]
                    }
                    if (i.selectedAdd[1] != 0) {
                        add1 = i.add[1].option[i.selectedAdd[1]]
                    }
                    if (i.selectedAdd[2] != 0) {
                        add2 = i.add[2].option[i.selectedAdd[2]]
                    }
                }
                4 -> {
                    if (i.selectedAdd[0] != 0) {
                        add0 = i.add[0].option[i.selectedAdd[0]]
                    }
                    if (i.selectedAdd[1] != 0) {
                        add1 = i.add[1].option[i.selectedAdd[1]]
                    }
                    if (i.selectedAdd[2] != 0) {
                        add2 = i.add[2].option[i.selectedAdd[2]]
                    }
                    if (i.selectedAdd[3] != 0) {
                        add3 = i.add[3].option[i.selectedAdd[3]]
                    }
                }
                else -> return
            }


            val productToOrder = arrayListOf<String>(i.name, add0, add1, add2, add3, i.count.toString() + " шт", "\n")

            productArray.addAll(productToOrder)
        }

        var orderToDatabase =
            mapOf("products" to productArray, "comment" to order.comment, "time" to order.timeTo)


        var auth: FirebaseAuth = FirebaseAuth.getInstance()

        getUserFromDatabase(null, auth.currentUser!!.uid, orderToDatabase)



    }


}