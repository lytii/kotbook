package com.book.clue.kotbook

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.util.Network
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test_network() {
        val network = Network()
//        network.getBookList(this::printList)
    }

    fun printList(list: List<Book>) {
        for (item in list) {
            println("${item.bookTitle} ${item.url}")
        }
    }

}
