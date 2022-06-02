package com.example.mobileappproject.lists

interface TaskRowListener {
    fun onTaskChange(objectId: String, isDone: Boolean)
    fun onTaskDelete(objectId: String, desc: String)
}