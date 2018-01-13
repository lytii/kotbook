package com.book.clue.kotbook.managers

import android.util.Log
import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.BookDao
import com.book.clue.kotbook.util.Network
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookListManager @Inject constructor(val bookDao: BookDao, val network: Network) {

    fun getAllBooks() = getBooks()

    fun getBooks() =
        bookDao.getAllBooks()
                .subscribeOn(Schedulers.io())
                .flatMap { list ->
                    if (list.isNotEmpty()) {
                        Log.d("Book", "loading from db")
                        Single.just(list)
                    } else {
                        Log.d("Book", "loading from network")
                        getFromNetwork()
                    }
                }

    fun getFromNetwork() =
        network.getGravityBooklist()
                .map { addAllBooks(it)  }

    fun addAllBooks(bookList: List<Book>): List<Book> {
        bookDao.addAllBooks(*bookList.toTypedArray())
        return bookList
    }

}
