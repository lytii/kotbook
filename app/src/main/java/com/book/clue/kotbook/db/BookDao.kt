package com.book.clue.kotbook.db

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface BookDao {

    @Query("SELECT * FROM Book")
    fun getAllBooks(): Single<List<Book>>

    @Insert
    fun addBook(book: Book)

    @Insert
    fun addAllBooks(vararg books: Book)

    @Query("SELECT * FROM Book WHERE name LIKE :name")
    fun getBookByTitle(name: String): Book
}