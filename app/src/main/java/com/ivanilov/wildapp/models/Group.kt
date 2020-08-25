package com.ivanilov.wildapp.models

import com.google.firebase.database.DataSnapshot
import com.ivanilov.wildapp.utils.Constants
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
            @Suppress("UNCHECKED_CAST")
            val value: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            this.name = value[Constants.GROUP_ITEM_NAME] as String
            this.available = value[Constants.GROUP_ITEM_AVAILABLE] as Boolean

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

}