package com.example.mobileappproject

import com.google.firebase.firestore.Exclude

class Task {
    companion object Factory {
        fun create(): Task = Task()

    }

    var objectId: String? = null
    var taskDesc: String? = null
    var done: Boolean = false

    @Exclude
    fun toMap(): Map<String, Any>{
        return mapOf(
            "done" to done
        )
    }
}
