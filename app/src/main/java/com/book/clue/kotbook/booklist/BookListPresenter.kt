package com.book.clue.kotbook.booklist

import com.book.clue.kotbook.util.BookNetwork
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class BookListPresenter (
//        val activty: Activity,
//        var bookNetwork: Network
) {
    var bookNetwork: BookNetwork
    init {
        bookNetwork = buildRetroFit()
    }

    fun buildRetroFit() = Retrofit.Builder()
            .baseUrl("https://www.wuxiaworld.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(BookNetwork::class.java)

    fun getBookList() {
        bookNetwork.getFromUrl("https://www.wuxiaworld.com/").subscribe()
    }

}