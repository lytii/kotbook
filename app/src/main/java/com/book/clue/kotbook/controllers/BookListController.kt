package com.book.clue.kotbook.controllers

import android.arch.persistence.room.Room
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.book.clue.kotbook.R
import com.book.clue.kotbook.booklist.BookListAdapter
import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.BookDao
import com.book.clue.kotbook.db.BookDatabase
import com.book.clue.kotbook.util.DaggerNetworkComponent
import com.book.clue.kotbook.util.Network
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book_list.view.*
import javax.inject.Inject

class BookListController : Controller() {

    @Inject
    lateinit var network: Network

    var bookDatabase: BookDatabase? = null
    var bookDao: BookDao? = null

    lateinit var booklistView: RecyclerView
    val title = "WuxiaWorld"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

        if (bookDatabase == null) {
            bookDatabase = applicationContext?.let {
                Room.databaseBuilder(it, BookDatabase::class.java, "bookdb").build()
            }
            bookDao = bookDatabase?.bookDao()
        }

        DaggerNetworkComponent.builder().build().inject(this)

        val view = inflater.inflate(R.layout.activity_book_list, container, false)
        booklistView = view.book_list
        booklistView.layoutManager = LinearLayoutManager(inflater.context)

        bookDao?.getAllBooks()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ list ->
                    if (list != null && list.isNotEmpty()) {
                        Log.d("not null", "llll")
                        createRecyclerView(list)
                    } else {
                        Log.d("null", "llll")
                        loadFromNetwork()
                    }
                })
        return view
    }

    private fun loadFromNetwork() {
        Log.d("loadfromNetwork", "llll")
        network.getBookList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    saveBooks(it)
                }
                .subscribe(this::createRecyclerView)
    }

    private fun saveBooks(bookList: List<Book>) {
        Log.d("saveBooks", "llll")
        Single.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe { _ -> bookDao?.addAllBooks(*bookList.toTypedArray()) }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = title
    }

    fun createRecyclerView(bookList: List<Book>) {
        booklistView.adapter = BookListAdapter(bookList, this::showChapterList)
    }

    fun toast(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showChapterList(title: String, url: String) {
        val changeController = ChapterListController(title, url)
        router.pushController(
                RouterTransaction.with(changeController)
                        .pushChangeHandler(HorizontalChangeHandler())
                        .popChangeHandler(HorizontalChangeHandler())
        )
    }

}