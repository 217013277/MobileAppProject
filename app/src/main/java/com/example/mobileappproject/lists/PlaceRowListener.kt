package com.example.mobileappproject.lists

interface PlaceRowListener {
    fun onFavClick(objectId: String, isFav: Boolean)
    fun onPlaceDelete(objectId: String, placeName: String)
}