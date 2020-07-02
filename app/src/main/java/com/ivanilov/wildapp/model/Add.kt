package com.ivanilov.wildapp.model

class Add {
    val name: String
    val option: MutableList<String>
    val costOption: MutableList<Int>

    constructor(name: String, option:MutableList<String>, costOption: MutableList<Int>) {
        this.name = name
        this.option = option
        this.costOption = costOption
    }
}