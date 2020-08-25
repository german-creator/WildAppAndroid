package com.ivanilov.wildapp.models

class Order {

    var products: ArrayList<ProductInBasket>
    var comment: String
    var timeTo: String

    constructor(products: ArrayList<ProductInBasket>, comment: String, timeTo: String) {
        this.products = products
        this.comment = comment
        this.timeTo = timeTo
    }

}