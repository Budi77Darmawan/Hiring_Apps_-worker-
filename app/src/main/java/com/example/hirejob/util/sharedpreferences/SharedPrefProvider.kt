package com.example.hirejob.util.sharedpreferences

import android.content.Context

class SharedPrefProvider (context: Context) {

    private var sharedPref = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

    fun putBoolean (key: String?, value: Boolean) {
        sharedPref.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun putString (key: String?, value: String?) {
        sharedPref.edit()
            .putString(key, value)
            .apply()
    }

    fun getBoolean (key: String? ): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun getString (key: String? ): String? {
        return sharedPref.getString(key, null)
    }

    fun resetSharedPref () {
        sharedPref.edit()
            .clear()
            .apply()
    }

}