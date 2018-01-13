package com.book.clue.kotbook.util

import android.util.Log
import com.book.clue.kotbook.db.Book
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class Network @Inject constructor() {
    val bookNetwork: BookNetwork
    val WUXIA_URL = "http://www.wuxiaworld.com/"
    val GRAVITY_TALES_URL = "http://gravitytales.com"
    var baseUrl = GRAVITY_TALES_URL

    constructor(baseUrl: String) : this() {
        this.baseUrl = baseUrl
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        bookNetwork = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(BookNetwork::class.java)
    }

    fun getBookList(): Single<List<Book>> {
        return bookNetwork.getFromUrl(baseUrl)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForBookList(it) }
                .map(Collection<Book>::sorted)
    }

    fun getChapterList(url: String): Single<ArrayList<Book>> {
        val fullUrl = if (url.startsWith("http")) url else GRAVITY_TALES_URL+url
        Log.d("getChapterList", fullUrl)
        return bookNetwork.getFromUrl(fullUrl)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForChapterList(it) }
    }

    fun getChapter(url: String): Single<MutableList<String>> {
//        Log.d("getChapter", url)
        return bookNetwork.getFromUrl(url)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForChapter(it) }
    }

    fun getGravityBooklist(): Single<List<Book>> {
        return bookNetwork
                .getFromUrl(GRAVITY_TALES_URL)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { it.select("a[href]") }
                .map { list -> list.filter { it.attr("href").startsWith("/novel") } }
                .map { it.sortedBy { it.text() } }
                .map { list -> list.map { Book(it.text(), it.attr("href")) } }
    }
}
