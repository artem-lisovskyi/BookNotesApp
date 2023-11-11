package com.booknotes.booknotesapp.data

import android.content.Context

class SaveShared {
    companion object {
        private const val PREF_NAME = "ButtonState_"

        fun setFavorite(context: Context?, key: String, value: Boolean, userId:String) {
            val sharedPreferences = context?.getSharedPreferences(PREF_NAME + userId, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(key, value)
            editor?.apply()
        }

        fun getFavorite(context: Context?, key: String, userId:String): Boolean {
            val sharedPreferences = context?.getSharedPreferences(PREF_NAME + userId, Context.MODE_PRIVATE)
            return sharedPreferences?.getBoolean(key, false) ?: false
        }
    }
}
