package com.book.clue.kotbook

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.util.Network
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun emptyTest() {
        val list: List<String> = listOf()

        Single.just(list)
                .filter { it.isNotEmpty() }
                .switchIfEmpty(Maybe.just(get()))
                .subscribe { print(it) }

    }

    @Test
    fun rxtTest() {
        val list: List<String> = listOf("tree")

        Single.just(list)
                .filter { it.isNotEmpty() }
                .switchIfEmpty(Maybe.just(get()))
                .subscribe { print(it) }

    }

    fun get(): List<String> {
        return listOf("get")
    }
}
