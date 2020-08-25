package com.ivanilov.wildapp.models

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Add {
    val name: String
    val option: ArrayList<String>
    val costOption: ArrayList<Long>

    constructor(name: String, option: ArrayList<String>, costOption: ArrayList<Long>) {
        this.name = name
        this.option = option
        this.costOption = costOption
    }

}