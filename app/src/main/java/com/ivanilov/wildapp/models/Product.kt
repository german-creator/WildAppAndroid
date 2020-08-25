package com.ivanilov.wildapp.models

import com.google.firebase.database.DataSnapshot
import com.ivanilov.wildapp.utils.Constants
import java.lang.Exception

open class Product {

    lateinit var group: String
    lateinit var name: String
    lateinit var description: String
    var cost: Long = 0
    var volume: Long = 0
    lateinit var add: ArrayList<Add>
    var available: Boolean = true
    lateinit var imageUrl: String

    constructor(
        group: String,
        name: String,
        description: String,
        cost: Long,
        volume: Long,
        add: ArrayList<Add>,
        available: Boolean
    ) {
        this.group = group
        this.name = name
        this.description = description
        this.cost = cost
        this.volume = volume
        this.add = add
        this.available = available
        this.imageUrl = ""
    }

    constructor() {
        this.group = "Классика"
        this.name = "Name"
        this.description = "Description"
        this.cost = 150
        this.volume = 20
        var add: ArrayList<Add> = ArrayList()
        val option = arrayListOf("Cow", "Coconut", "Soy")
        val costOption: ArrayList<Long> = arrayListOf(0, 80, 80)
        add.add(Add("Milk", option, costOption))
        val option1 = arrayListOf("Sugar free", "One spoon", "Two spoon")
        add.add(Add("Sugar", option1, arrayListOf()))
        this.add = add
        this.available = true
        this.imageUrl =
            "https://firebasestorage.googleapis.com/v0/b/wild-coffee-order.appspot.com/o/%D0%9B%D0%B5%D1%82%D0%BD%D0%B5%D0%B5%20%D0%BC%D0%B5%D0%BD%D1%8E%2F%D0%90%D0%B8%CC%86%D1%81%20%D0%9C%D0%B0%D1%82%D1%87%D0%B0.png?alt=media&token=4b847c62-4e09-4015-bd7f-60d84eefaf16"
    }

    constructor(snapshot: DataSnapshot) {
        try {

            @Suppress("UNCHECKED_CAST")
            val value: HashMap<String, Any> = snapshot.value as HashMap<String, Any>

            this.group = value[Constants.PRODUCT_ITEM_GROUP] as String
            this.name = value[Constants.PRODUCT_ITEM_NAME] as String
            this.description = value[Constants.PRODUCT_ITEM_DESCRIPTION] as String
            this.cost = value[Constants.PRODUCT_ITEM_COST] as Long
            this.volume = value[Constants.PRODUCT_ITEM_VOLUME] as Long


            var add: ArrayList<Add> = ArrayList()

            @Suppress("UNCHECKED_CAST")
            val addName = value[Constants.PRODUCT_ITEM_ADD] as ArrayList<String>

            for (i in 0..addName.count() - 1) {

                var option: ArrayList<String> = ArrayList()

                if (value["${Constants.PRODUCT_ITEM_ADD_OPTION}${i}"] != null) {
                    @Suppress("UNCHECKED_CAST")
                    option = value["${Constants.PRODUCT_ITEM_ADD_OPTION}${i}"] as ArrayList<String>
                }

                var cost: ArrayList<Long> = ArrayList()
                if (value["${Constants.PRODUCT_ITEM_ADD_COST_OPTION}${i}"] != null) {
                    @Suppress("UNCHECKED_CAST")
                    cost = value["${Constants.PRODUCT_ITEM_ADD_COST_OPTION}${i}"] as ArrayList<Long>
                }

                add.add(Add(addName[i], option, cost))
            }
            this.add = add
            this.available = value[Constants.PRODUCT_ITEM_AVAILABLE] as Boolean
            this.imageUrl = value[Constants.PRODUCT_ITEM_IMAGE_URL] as String


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getTestProductList(): ArrayList<Product> {
        var productList: ArrayList<Product> = ArrayList()
        for (i in 1..5) {
            val product = Product()
            product.name = product.name + " $i"
            productList.add(product)
        }
        return productList
    }


}