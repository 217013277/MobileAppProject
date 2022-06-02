package com.example.mobileappproject

interface PlaceRowListener {
    fun onFavClick(objectId: String, isFav: Boolean)
    fun onPlaceDelete(objectId: String, placeName: String)
}