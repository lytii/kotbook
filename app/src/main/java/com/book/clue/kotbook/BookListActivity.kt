package com.book.clue.kotbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.book.clue.kotbook.booklist.BookListAdapater
import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_main.*

class BookListActivity : Activity() {

    companion object {
        val EXTRA_BOOK_URL = "com.book.clue.kotbook.bookUrl"
    }
    lateinit var view: RecyclerView
    val network = Network()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = recycler_view

        title = "Book List"
        network.getBookList(this::setAdapter)
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    fun setAdapter(bookList: List<BookListItem>) {
        val booklistAdapter = BookListAdapater(bookList, this::showChapterList)
        view.adapter = booklistAdapter
    }

    fun showChapterList(url: String) {
        val intent = Intent(this, ChapterListActivity::class.java)
        intent.putExtra(EXTRA_BOOK_URL, url)
        startActivity(intent)
    }
}

