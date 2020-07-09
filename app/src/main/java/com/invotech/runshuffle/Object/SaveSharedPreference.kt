
package com.invotech.runshuffle.Object

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SaveSharedPreference {
    fun getPreferences(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    fun setLoggedIn(
        context: Context?,
        loggedIn: Boolean
    ) {
        val editor = getPreferences(
            context
        ).edit()
        editor.putBoolean("logged_in_status", loggedIn)
        /*editor.putString("User Name",Username)
        editor.putString("Password",Password)*/
        editor.apply()
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    fun getLoggedStatus(context: Context?): Boolean {
        return getPreferences(
            context
        ).getBoolean("logged_in_status", false)
    }
}