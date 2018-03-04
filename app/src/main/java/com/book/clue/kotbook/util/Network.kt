package com.book.clue.kotbook.util

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.db.Paragraph
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Inject


class Network @Inject constructor() {
    val wBookApi: WBookApi
    val WUXIA_URL = "http://www.wuxiaworld.com/"
    //    val GRAVITY_TALES_URL = "http://gravitytales.com"
    var GRAVITY_TALES_URL = "http://gravitytales.com/api/"
    var baseUrl = "http://gravitytales.com/api/"

    constructor(baseUrl: String) : this() {
        this.baseUrl = baseUrl
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        wBookApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(WBookApi::class.java)
    }

    fun getBookListN(): Single<List<Book>> {
        return wBookApi.getBookList()
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<Book>::class.java).asList() }
                .map {
                    it.forEach { it.url }
                    it
                }
    }

    fun getBookList(): Single<List<Book>> {
        return wBookApi.getFromUrl(baseUrl)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForBookList(it) }
                .map(Collection<Book>::sorted)
    }

    fun getChapterList(bookId: Int): Single<List<Chapter>> {
        return wBookApi.getGroupList(bookId)
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<WBookApi.ChapterGroup>::class.java).asList() }
                .flatMapIterable { it }
                .map { println("A: $it"); it }
                .concatMapEager { Network().wBookApi.getGroupChapters(it.groupId) }
                .map { it.string() }
                .map { Gson().fromJson(it, Array<Chapter>::class.java).asList() }
                .flatMapIterable { it }
                .map {
                    it.bookId = bookId
                    it
                }
                .toList()

    }

    fun getChapter(chapterId: Int): Single<MutableList<String>> {
        return wBookApi.getChapter(chapterId)
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, ChapterContent::class.java) }
                .map { Jsoup.parseBodyFragment(it.content).body() }
                .map { it.select("p").map { it.text() }.filter { it != "" }.toMutableList() }
                .firstOrError()
    }

    fun getGravityBooklist(): Single<List<Book>> {
        return wBookApi.getBookList()
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<Book>::class.java).asList() }
                .map {
                    it.forEach { it.url }
                    it
                }
    }
}
