package com.book.clue.kotbook.controllers

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.book.clue.kotbook.R
import com.book.clue.kotbook.booklist.BookListAdapter
import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_book_list.view.*

class BookListController : Controller() {

    val network = Network.instance
    lateinit var booklistView: RecyclerView
    val title = "WuxiaWorld"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        var view = inflater.inflate(R.layout.activity_book_list, container, false)
        booklistView = view.book_list
        booklistView.layoutManager = LinearLayoutManager(inflater.context)
        network.getBookList { bookItems -> createRecyclerView(bookItems) }
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = title
    }

    fun createRecyclerView(bookList: List<BookListItem>) {
        booklistView.adapter = BookListAdapter(bookList, this::showChapterList)
    }

    fun toast(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showChapterList(title: String, url: String) {
        router.pushController(RouterTransaction.with(ChapterListController(title, url)))
    }

}