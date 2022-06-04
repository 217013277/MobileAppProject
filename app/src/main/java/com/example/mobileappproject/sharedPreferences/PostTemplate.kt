package com.example.mobileappproject.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PostTemplate {
    private const val NAME= "name"
    private const val DESC= "desc"
    private const val LATITUDE= "latitude"
    private const val LONGITUDE= "longitude"
    private const val ADDRESS= "address"
    private const val WEATHER= "weather"

    private  fun getSharedPreference(ctx: Context): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context, const: String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getPlaceDesc(context: Context)= getSharedPreference(
        context
    )?.getString(DESC,"")

    fun setPlaceDesc(context: Context, desc: String){
        editor(
            context,
            DESC,
            desc
        )
    }

    fun setPlaceName(context: Context, name:String){
        editor(
            context,
            NAME,
            name
        )
    }

    fun getPlaceName(context: Context) = getSharedPreference(
        context
    )?.getString(NAME,"")

    fun setLatitude(context: Context, latitude:String){
        editor(
            context,
            LATITUDE,
            latitude
        )
    }

    fun getLatitude(context: Context) = getSharedPreference(
        context
    )?.getString(LATITUDE,"")

    fun setLongitude(context: Context, longitude:String){
        editor(
            context,
            LONGITUDE,
            longitude
        )
    }

    fun getLongitude(context: Context) = getSharedPreference(
        context
    )?.getString(LONGITUDE,"")

    fun setAddress(context: Context, address:String){
        editor(
            context,
            ADDRESS,
            address
        )
    }

    fun getAddress(context: Context) = getSharedPreference(
        context
    )?.getString(ADDRESS,"")

    fun setWeather(context: Context, weather:String){
        editor(
            context,
            WEATHER,
            weather
        )
    }

    fun getWeather(context: Context) = getSharedPreference(
        context
    )?.getString(WEATHER,"")
}