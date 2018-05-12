package com.book.clue.kotbook

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
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
        Maybe.empty<String>()
        Maybe.error<Exception>(Exception("hi"))
                .subscribeBy (
                        onError = { println("error") },
                        onSuccess = { println(it)} ,
                        onComplete = { println("complete")}
                )

    }

    fun success(list: List<String>) {

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
