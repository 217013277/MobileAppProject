package com.example.mobileappproject.lists

object PlaceStatics {
    @JvmStatic val FIREBASE_TASK: String = "place"
}

class Place {
    companion object Factory {
        fun create(): Place = Place()
    }

    var objectId: String? = null
    var placeName: String? = null
    var placeDesc: String? = null
    var isFav: Boolean? = false
}
