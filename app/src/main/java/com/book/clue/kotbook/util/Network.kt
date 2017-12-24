package com.book.clue.kotbook.util

import android.util.Log
import com.book.clue.kotbook.db.Book
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Network {
    val bookNetwork: BookNetwork
    val WUXIA_URL = "http://www.wuxiaworld.com/"

    init {
        bookNetwork = Retrofit.Builder()
                .baseUrl(WUXIA_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(BookNetwork::class.java)
    }

    fun getBookList() : Single<List<Book>> {
        return bookNetwork.getFromUrl(WUXIA_URL)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForBookList(it) }
                .map(Collection<Book>::sorted)
    }

    fun getChapterList(url: String, listener: (ArrayList<Book>) -> Unit) {
        Log.d("getChapterList", url)
        bookNetwork.getFromUrl(url)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForChapterList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener)
    }

    fun getChapter(url: String, listener: (ArrayList<String>) -> Unit) {
        Log.d("getChapter", url)
        bookNetwork.getFromUrl(url)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForChapter(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener)
    }
}
