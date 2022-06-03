package com.example.mobileappproject.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PostTemplate {
    private const val NAME= "name"
    private const val DESC= "desc"

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
}