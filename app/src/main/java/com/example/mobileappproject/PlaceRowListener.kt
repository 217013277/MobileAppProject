package com.example.mobileappproject

interface PlaceRowListener {
    fun onFavClick(objectId: String, isDone: Boolean)
    fun onPlaceDelete(objectId: String, placeName: String)
}