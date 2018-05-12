package com.book.clue.kotbook.util

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.db.Paragraph
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class Network @Inject constructor() {
    val gBookApi: GBookApi
    val wBookApi: WuxiaApi
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
        val callAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        val converterFactory = MoshiConverterFactory.create()
        gBookApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(callAdapter)
                .addConverterFactory(converterFactory)
                .build()
                .create(GBookApi::class.java)
        wBookApi = Retrofit.Builder()
                .baseUrl(WUXIA_URL)
                .client(client)
                .addCallAdapterFactory(callAdapter)
                .addConverterFactory(converterFactory)
                .build()
                .create(WuxiaApi::class.java)
    }

    fun getBookListN(): Single<List<Book>> {
        return gBookApi.getBookList()
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<Book>::class.java).asList() }
                .map {
                    it.forEach { it.url }
                    it
                }
    }

    fun getBookList(): Single<List<Book>> {
        return gBookApi.getFromUrl(baseUrl)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { Parser.parseForBookList(it) }
                .map(Collection<Book>::sorted)
    }

    fun getChapterList(book: Book): Single<List<Chapter>> {
        return gBookApi.getGroupList(book.id)
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<GBookApi.ChapterGroup>::class.java).asList() }
                .flatMapIterable { it }
                .map { println("A: $it"); it }
                .concatMapEager { Network().gBookApi.getGroupChapters(it.groupId) }
                .map { it.string() }
                .map { Gson().fromJson(it, Array<Chapter>::class.java).asList() }
                .flatMapIterable { it }
                .map {
                    it.bookId = book.id
                    it
                }
                .toList()
    }

    fun getWuxiaChapterList(book: Book): Single<List<Chapter>> {
        return wBookApi.getUrl(book.url)
            .map(ResponseBody::string)
            .map(Jsoup::parse)
            .map { Parser.parseWChapterList(it, book.id)}
    }

    fun getWuxiaChapter(chapter: Chapter): Single<List<String>> {
        return wBookApi.getUrl(chapter.suffixUrl)
            .map(ResponseBody::string)
            .map(Jsoup::parse)
            .map { Parser.parseWChapter(it, chapter) }
    }

    fun getChapter(chapter: Chapter): Single<MutableList<String>> {
        return gBookApi.getChapter(chapter.id)
            .map(ResponseBody::string)
            .map { Gson().fromJson(it, ChapterContent::class.java) }
            .map { Jsoup.parseBodyFragment(it.content).body() }
            .map { it.select("p").map { it.text() }.filter { it != "" }.toMutableList() }
            .firstOrError()
    }

    fun getGravityBooklist(): Single<List<Book>> {
        return gBookApi.getBookList()
                .map(ResponseBody::string)
                .map { Gson().fromJson(it, Array<Book>::class.java).asList() }
                .map {
                    it.forEach { it.url }
                    it
                }
    }

    fun getWuxiaBookList(): Single<List<Book>> {
        return wBookApi.getList("korean")
            .map(ResponseBody::string)
            .map(Jsoup::parse)
            .map { Parser.parseWBookList(it) }
    }
}
