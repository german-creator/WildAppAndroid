package com.ivanilov.wildapp.model

class Product {

    val group: String
    var name: String
    val description: String
    val cost: Int
    val volume: Int
    val add: MutableList<Add>
    val avalible: Boolean
    val imageUrl: String

    constructor(group: String, name: String, description: String, cost: Int, volume: Int, add: MutableList<Add>, avalible: Boolean) {
        this.group = group
        this.name = name
        this.description = description
        this.cost = cost
        this.volume = volume
        this.add = add
        this.avalible = avalible
        this.imageUrl = ""
    }

    constructor() {
        this.group = "Классика"
        this.name = "Name"
        this.description = "Description"
        this.cost = 150
        this.volume = 20
        var add: MutableList<Add> = mutableListOf()
        val option = mutableListOf("Cow", "Coconut", "Soy")
        val costOption = mutableListOf(0, 80, 80)
        add.add(Add( "Milk",  option,  costOption))
        val option1 = mutableListOf("Sugar free", "One spoon", "Two spoon")
        add.add(Add( "Sugar",  option1, mutableListOf()))
        this.add = add
        this.avalible = true
        this.imageUrl = "https://firebasestorage.googleapis.com/v0/b/wild-coffee-order.appspot.com/o/%D0%9B%D0%B5%D1%82%D0%BD%D0%B5%D0%B5%20%D0%BC%D0%B5%D0%BD%D1%8E%2F%D0%90%D0%B8%CC%86%D1%81%20%D0%9C%D0%B0%D1%82%D1%87%D0%B0.png?alt=media&token=4b847c62-4e09-4015-bd7f-60d84eefaf16"
    }

    fun getTestProductList(): MutableList<Product> {
        var productList: MutableList<Product> = mutableListOf()
        for (i in 1..5) {
            val product = Product ()
            product.name = product.name + " $i"
            productList.add(product)
        }
        return productList
    }



}