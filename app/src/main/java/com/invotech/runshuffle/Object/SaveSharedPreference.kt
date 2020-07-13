@file:Suppress("DEPRECATION")

package com.invotech.runshuffle.Object
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
object SaveSharedPreference {
    fun getPreferences(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun setLoggedIn(
        context: Context?,
        loggedIn: Boolean
    ) {
        val editor = getPreferences(context).edit()
        editor.putBoolean("logged_in_status", loggedIn)

        editor.apply()
    }


    fun getLoggedStatus(context: Context?): Boolean {
        return getPreferences(context).getBoolean("logged_in_status", false)

    }
}