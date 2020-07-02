package com.ivanilov.wildapp.model

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Group {
    lateinit var name: String
    var available: Boolean = false

    constructor(name: String, avalible: Boolean) {
        this.name = name
        this.available = avalible
    }

    constructor(snapshot: DataSnapshot) {
        try {
            val value: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            this.name = value["name"] as String
            this.available = value["avalible"] as Boolean

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

}