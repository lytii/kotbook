package com.book.clue.kotbook.controllers

import android.content.Intent
import android.support.annotation.UiThread
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.book.clue.kotbook.managers.BookListManager
import com.book.clue.kotbook.R
import com.book.clue.kotbook.booklist.BookListAdapter
import com.book.clue.kotbook.dagger.NetworkComponent
import com.book.clue.kotbook.db.Book
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_book_list.view.*
import javax.inject.Inject



class BookListController : Controller() {

    @Inject
    lateinit var listManager: BookListManager

    lateinit var booklistView: RecyclerView
    val title = "WuxiaWorld"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        NetworkComponent.get().inject(this)

        val view = inflater.inflate(R.layout.activity_book_list, container, false)
        booklistView = view.book_list
        booklistView.layoutManager = LinearLayoutManager(inflater.context)

        listManager.getAllBooks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayBookList)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = title
    }

    @UiThread
    fun displayBookList(bookList: List<Book>) {
        booklistView.adapter = BookListAdapter(bookList.sorted(), this::showChapterList)
    }

    fun toast(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showChapterList(book: Book) {
        val changeController = ChapterListController(book)
        router.pushController(
                RouterTransaction.with(changeController)
                        .pushChangeHandler(HorizontalChangeHandler())
                        .popChangeHandler(HorizontalChangeHandler())
        )
    }
}
