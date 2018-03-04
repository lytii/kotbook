package com.book.clue.kotbook

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.book.clue.kotbook.db.BookDao
import com.book.clue.kotbook.db.BookDatabase
import org.junit.Before
import org.junit.Assert


@RunWith(AndroidJUnit4::class)
class DBTest {

    lateinit var bookDao: BookDao
    lateinit var bookDatabase: BookDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java).build()
        bookDao = bookDatabase.bookDao()
    }

//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        bookDatabase.close()
//    }

//    @Test
    fun db() {
        val myUrl = "a.b.c"
        val book = Book()
        book.url = myUrl
        bookDao.addBook(book)
        val booklist: List<Book> = bookDao.getAllBooks()

        Assert.assertEquals(booklist[0].url , book.url)
        Assert.assertEquals(booklist.size, 1)
    }

//    @Test
    fun faildb() {
        val myUrl = "a.b.c"
        val book = Book(myUrl)
        bookDao.addBook(book)
        val booklist: List<Book> = bookDao.getAllBooks()
        Assert.assertEquals(booklist[0].url , book.url)
        Assert.assertEquals(booklist.size, 1)
        println("ABC")
    }

    @Test
    fun getBook() {
        val bookA = Book(url = "url a", title = "Book_A")
        val bookB = Book(url = "url b", title = "Book_B")

        bookDao.addAllBooks(bookA)

        var book = bookDao.getBookByTitle(bookA.title)
        Assert.assertEquals(book.title, bookA.title)

        book = bookDao.getBookByTitle(bookB.title)
        Assert.assertEquals(book, null)
    }
}