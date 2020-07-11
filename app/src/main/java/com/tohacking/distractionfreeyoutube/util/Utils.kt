package com.tohacking.distractionfreeyoutube.util

import android.content.Context
import android.preference.PreferenceManager


class Utils {
    fun setDefaults(
        key: String?,
        value: String?,
        context: Context?
    ) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getDefaults(key: String?, context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, null)
    }
}