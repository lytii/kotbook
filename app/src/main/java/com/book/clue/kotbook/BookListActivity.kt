package com.book.clue.kotbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.book.clue.kotbook.booklist.BookListAdapter
import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_main.*

class BookListActivity : Activity() {

    companion object {
        val EXTRA_BOOK_URL = "com.book.clue.kotbook.bookUrl"
        val EXTRA_RELAUNCH = "com.book.clue.kotbook.relaunch"
        val EXTRA_CHAPTER_URL = "com.book.clue.kotbook.chapterUrl"
    }

    lateinit var view: RecyclerView
    val network = Network()
    var relaunch = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)

        val bookUrl = sharedPrefs.getString(getString(R.string.book_url_key), null)
        if (bookUrl != null) {
            relaunch = true
            showChapterList(bookUrl)
            return
        }

    }

    override fun onStart() {
        super.onStart()
        title = "Book List"
        view = recycler_view
        network.getBookList(this::setBookList)
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    fun setBookList(bookList: List<BookListItem>) {
        val booklistAdapter = BookListAdapter(bookList, this::showChapterList)
        view.adapter = booklistAdapter
    }

    fun showChapterList(url: String) {
        val intent = Intent(this, ChapterListActivity::class.java)
        if (relaunch) {
            val chapterUrl = sharedPrefs.getString(getString(R.string.chapter_url_key), null)
            intent.putExtra(EXTRA_CHAPTER_URL, chapterUrl)
            intent.putExtra(EXTRA_RELAUNCH, true)
            relaunch = false
        }
        intent.putExtra(EXTRA_BOOK_URL, url)
        startActivity(intent)
    }
}
