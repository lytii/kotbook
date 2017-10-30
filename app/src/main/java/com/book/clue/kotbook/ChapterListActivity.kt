package com.book.clue.kotbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.book.clue.kotbook.booklist.BookListAdapter
import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_book_list.*

class ChapterListActivity : Activity() {

    companion object {
        val EXTRA_CHAPTER_URL = "com.book.clue.kotbook.chapterUrl"
    }

    lateinit var bookUrl: String
    val network = Network()
    lateinit var view: RecyclerView
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        title = "Chapter List"
        view = book_list
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        sharedPrefs = getSharedPreferences(getString(com.book.clue.kotbook.R.string.shared_prefs), Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        // check relaunch
        val intent = intent
        val relaunch = intent.getBooleanExtra(BookListActivity.EXTRA_RELAUNCH, false)
        if (relaunch) {
            intent.putExtra(BookListActivity.EXTRA_RELAUNCH, false)
            showChapter(intent.getStringExtra(BookListActivity.EXTRA_CHAPTER_URL))
            return
        }

        val url = intent.getStringExtra(BookListActivity.EXTRA_BOOK_URL)
        if (url != null) {
            bookUrl = url
            var chapterList = sharedPrefs.getStringSet(url, null)
            if (chapterList == null) {
                getChapterList(bookUrl)
            } else {

            }
        }
    }

    fun getChapterList(url: String) {
        sharedPrefs.edit().putString(getString(R.string.book_url_key), url).apply()
        network.getChapterList(url, this::setChapterList)
    }

    fun setChapterList(chapterList: ArrayList<BookListItem>) {
        view.adapter = BookListAdapter(chapterList, this::showChapter)
    }

    fun showChapter(url: String) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra(EXTRA_CHAPTER_URL, url)
        startActivity(intent)
    }
}
