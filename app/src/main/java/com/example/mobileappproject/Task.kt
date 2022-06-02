package com.example.mobileappproject

object Statics {
    @JvmStatic val FIREBASE_TASK: String = "task"
}

class Task {
    companion object Factory {
        fun create(): Task = Task()
    }

    var objectId: String? = null
    var taskDesc: String? = null
    var done: Boolean? = false
}
