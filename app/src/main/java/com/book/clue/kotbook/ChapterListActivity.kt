package com.book.clue.kotbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.book.clue.kotbook.booklist.BookListAdapater
import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_book_list.*

class ChapterListActivity : Activity() {

    companion object {
        val EXTRA_CHAPTER_URL = "com.book.clue.kotbook.chapterUrl"
    }
    lateinit var view: RecyclerView
    val network = Network()
    lateinit var bookUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        title = "Chapter List"
        view = book_list
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val intent = intent
        val url = intent.getStringExtra(BookListActivity.EXTRA_BOOK_URL)
        if (url != null) {
            bookUrl = url
            getChapterList(bookUrl)
        }
    }

    fun setChapterList(chapterList: ArrayList<BookListItem>) {
        view.adapter = BookListAdapater(chapterList, this::showChapter)
    }

    fun showChapter(url: String) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra(EXTRA_CHAPTER_URL, url)
        startActivity(intent)
    }

    fun getChapterList(url: String) {
        network.getChapterList(url, this::setChapterList)
    }

    fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
