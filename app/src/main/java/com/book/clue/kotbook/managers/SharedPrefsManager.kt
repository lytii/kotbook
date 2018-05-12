package com.book.clue.kotbook.managers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsManager @Inject constructor(val context: Context) {
    private val sharedPreferences: SharedPreferences
            = context.getSharedPreferences(BOOK_PREF, MODE_PRIVATE)
    companion object {
        const val CURRENT_CHAPTER = "CURRENT_CHAPTER"
        const val BOOK_PREF = "BOOK_PREF"
    }

    fun getCurrentChapterId(): Int {
        val current = sharedPreferences.getInt(CURRENT_CHAPTER, -1)
        Log.d("SharedPrefsManager", "Getting chapter $current")
        return current
    }

    fun saveChapterId(chapterId: Int) {
        Log.d("SharedPrefsManager", "Saving chapter $chapterId")
        if (sharedPreferences.getInt(CURRENT_CHAPTER, -1) < chapterId) {
            getSharedPref().putInt(CURRENT_CHAPTER, chapterId).apply()
        }
    }

    private fun getSharedPref(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

}