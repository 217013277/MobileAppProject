package com.example.mobileappproject

object PlaceStatics {
    @JvmStatic val FIREBASE_TASK: String = "place"
}

class Place {
    companion object Factory {
        fun create(): Place = Place()
    }

    var placeObjectId: String? = null
    var placeName: String? = null
    var placeDesc: String? = null
    var placeFav: Boolean? = false
}
