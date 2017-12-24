package com.book.clue.kotbook.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface BookDao {

    @Query("SELECT * FROM Book")
    fun getAllBooks(): Single<List<Book>>

    @Insert
    fun addBook(book: Book)

    @Insert
    fun addAllBooks(vararg books: Book)

    @Query("SELECT * FROM Book WHERE bookTitle LIKE :bookTitle")
    fun getBookByTitle(bookTitle: String): Book

}