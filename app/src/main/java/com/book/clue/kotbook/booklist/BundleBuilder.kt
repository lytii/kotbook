package com.book.clue.kotbook.booklist

import android.os.Bundle

class BundleBuilder(val args: Bundle) {
    val CHAPTER_URL_KEY = "ChapterController.ChapterUrl"
    val TITLE_KEY = "ChapterController.Title"

    fun putString(url_key: String, url: String): BundleBuilder {
        args.putString(url_key, url)
        return this
    }

    fun build() = args

    fun saveChapterState(title: String, url: String): BundleBuilder {
        putString(CHAPTER_URL_KEY, url)
        putString(TITLE_KEY, title)
        return this
    }
}
