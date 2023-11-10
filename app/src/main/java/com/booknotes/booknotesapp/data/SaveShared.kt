package com.booknotes.booknotesapp.data

import android.content.Context

class SaveShared {
    companion object {
        private const val PREF_NAME = "ButtonState"

        fun setFavorite(context: Context?, key: String, value: Boolean) {
            val sharedPreferences = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(key, value)
            editor?.apply()
        }

        fun getFavorite(context: Context?, key: String): Boolean {
            val sharedPreferences = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences?.getBoolean(key, false) ?: false
        }
    }
}
