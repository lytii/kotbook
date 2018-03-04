package com.book.clue.kotbook.managers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefsManager @Inject constructor(val context: Context) {
    private val sharedPreferences: SharedPreferences
            = context.getSharedPreferences(BOOK_PREF, MODE_PRIVATE)
    companion object {
        const val CURRENT_CHAPTER = "CURRENT_CHAPTER"
        const val BOOK_PREF = "BOOK_PREF"
    }

    fun getCurrentChapterUrl(): String {
        return sharedPreferences.getString(CURRENT_CHAPTER, "")
    }

    fun saveChapterUrl(chapterUrl: String) {
        getSharedPref().putString(CURRENT_CHAPTER, chapterUrl).apply()
    }

    private fun getSharedPref(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

}