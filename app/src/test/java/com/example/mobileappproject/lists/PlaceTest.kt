package com.example.mobileappproject.lists

import org.junit.Test

import org.junit.Assert.*

internal class PlaceTest {

    @Test
    fun setAndGetObjectId() {
        val testPlace = Place.create()
        testPlace.objectId = "123"
        assertEquals("123",testPlace.objectId)
    }

//    @Test
//    fun getObjectId() {
//        val testPlace = Place.create()
//        testPlace.objectId = "123"
//        assertEquals("123",testPlace.objectId)
//    }

//    @Test
//    fun getPlaceName() {
//        val testPlace = Place.create()
//        testPlace.placeName = "address name"
//        assertEquals("address name",testPlace.placeName)
//    }

    @Test
    fun setAndGetPlaceName() {
        val testPlace = Place.create()
        testPlace.placeName = "address name"
        assertEquals("address name",testPlace.placeName)
    }

//    @Test
//    fun getPlaceDesc() {
//        val testPlace = Place.create()
//        testPlace.placeDesc = "address description"
//        assertEquals("address description",testPlace.placeDesc)
//    }

    @Test
    fun setAndGetPlaceDesc() {
        val testPlace = Place.create()
        testPlace.placeDesc = "address description"
        assertEquals("address description",testPlace.placeDesc)
    }

//    @Test
//    fun getPlaceLatitude() {
//        val testPlace = Place.create()
//        testPlace.placeLatitude = "-1.00"
//        assertEquals("-1.00",testPlace.placeLatitude)
//    }

    @Test
    fun setAndGetPlaceLatitude() {
        val testPlace = Place.create()
        testPlace.placeLatitude = "-1.00"
        assertEquals("-1.00",testPlace.placeLatitude)
    }

//    @Test
//    fun getPlaceLongitude() {
//        val testPlace = Place.create()
//        testPlace.placeLongitude = "1.00"
//        assertEquals("1.00",testPlace.placeLongitude)
//    }

    @Test
    fun setAndGetPlaceLongitude() {
        val testPlace = Place.create()
        testPlace.placeLongitude = "1.00"
        assertEquals("1.00",testPlace.placeLongitude)
    }

//    @Test
//    fun getPlaceAddress() {
//        val testPlace = Place.create()
//        testPlace.placeAddress = "abc123"
//        assertEquals("abc123",testPlace.placeAddress)
//    }

    @Test
    fun setAndGetPlaceAddress() {
        val testPlace = Place.create()
        testPlace.placeAddress = "abc123"
        assertEquals("abc123",testPlace.placeAddress)
    }

//    @Test
//    fun getPlaceWeather() {
//        val testPlace = Place.create()
//        testPlace.placeWeather = "cloud"
//        assertEquals("cloud",testPlace.placeWeather)
//    }

    @Test
    fun setAndGetPlaceWeather() {
        val testPlace = Place.create()
        testPlace.placeWeather = "cloud"
        assertEquals("cloud",testPlace.placeWeather)
    }

//    @Test
//    fun getPlaceTime() {
//        val testPlace = Place.create()
//        testPlace.placeTime = "12:00"
//        assertEquals("12:00",testPlace.placeTime)
//    }

    @Test
    fun setAndGetPlaceTime() {
        val testPlace = Place.create()
        testPlace.placeTime = "12:00"
        assertEquals("12:00",testPlace.placeTime)
    }

//    @Test
//    fun isFav() {
//        val testPlace = Place.create()
//        testPlace.isFav = true
//        assertEquals(true,testPlace.isFav)
//    }

    @Test
    fun setAndGetIsFav() {
        val testPlace = Place.create()
        testPlace.isFav = true
        assertEquals(true,testPlace.isFav)
    }

//    @Test
//    fun getImageUrl() {
//        val testPlace = Place.create()
//        testPlace.imageUrl = "www.abc.com"
//        assertEquals("www.abc.com",testPlace.imageUrl)
//    }

    @Test
    fun setAndGetImageUrl() {
        val testPlace = Place.create()
        testPlace.imageUrl = "www.abc.com"
        assertEquals("www.abc.com",testPlace.imageUrl)
    }
}