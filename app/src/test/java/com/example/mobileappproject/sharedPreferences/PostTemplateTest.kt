package com.example.mobileappproject.sharedPreferences

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

internal class PostTemplateTest {

//    PostTemplate.setPlaceName(this, etPlaceName.text.toString())
//    PostTemplate.setPlaceDesc(this, etPlaceDesc.text.toString())
//    PostTemplate.setLatitude(this, tvLatitude.text.toString())
//    PostTemplate.setLongitude(this, tvLongitude.text.toString())
//    PostTemplate.setAddress(this, tvAddress.text.toString())
//    PostTemplate.setWeather(this, tvWeather.text.toString())

    @Test
    fun getAndSetPlaceDesc() {
        val context = Mockito.mock(Context::class.java)
        val result = "Place description"
        PostTemplate.setPlaceDesc(context, result)
        val answer = PostTemplate.getPlaceDesc(context).toString()
        assertEquals(result, answer)
    }

    @Test
    fun setPlaceName() {
    }

    @Test
    fun getPlaceName() {
    }

    @Test
    fun setLatitude() {
    }

    @Test
    fun getLatitude() {
    }

    @Test
    fun setLongitude() {
    }

    @Test
    fun getLongitude() {
    }

    @Test
    fun setAddress() {
    }

    @Test
    fun getAddress() {
    }

    @Test
    fun setWeather() {
    }

    @Test
    fun getWeather() {
    }
}