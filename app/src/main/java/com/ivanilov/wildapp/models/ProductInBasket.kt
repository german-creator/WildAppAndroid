package com.ivanilov.wildapp.models

class ProductInBasket(): Product() {
    var count: Int = 1
    lateinit var selectedAdd: ArrayList<Int>
    var finalCost: Int = 0

    constructor(
        product: Product,
        count: Int,
        selectedAdd: ArrayList<Int>,
        finalCost: Int
    ) : this() {

        this.group = product.group
        this.name = product.name
        this.description = product.description
        this.cost = product.cost
        this.volume = product.volume
        this.add = product.add
        this.available = product.available
        this.imageUrl = product.imageUrl
        this.count = count
        this.selectedAdd = selectedAdd
        this.finalCost = finalCost
    }



}