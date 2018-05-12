package com.book.clue.kotbook

import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.util.WuxiaApi
import io.reactivex.internal.operators.observable.ObservableFromIterable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Clock

class WuxiaTest {

    val WUXIA_URL = "http://www.wuxiaworld.com/"
    val api = initApi()

    fun initApi(): WuxiaApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(WUXIA_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WuxiaApi::class.java)
    }

    @Test
    fun getTest() {
//        val i = 1
        for (i in 2..2) {
//        for (i in intArrayOf(1,2,3,201) ) {
            println("starting $i")
//            val body = api.getChapter("the-book-eating-magician", "bem-chapter-"+i).blockingGet()
            val body = api.getChapter("overgeared", "og-chapter-" + i).blockingGet()
//            val body = api.getUrl("/novel/seoul-stations-necromancer/ssn-chapter-1").blockingGet()

            var now = Clock.systemDefaultZone().millis()

            val document = Parser.parse(body.string(), WUXIA_URL) as Element
//            println("parser ${Clock.systemDefaultZone().millis() - now}ms")
//            now = Clock.systemDefaultZone().millis()

            val content = document.select(".fr-view").first().children()
//            println("content ${Clock.systemDefaultZone().millis() - now}ms")
//            now = Clock.systemDefaultZone().millis()

//            getNav(document)
            println("<hr> size = ${content.select("hr").size} ${Clock.systemDefaultZone().millis() - now}ms ")
//            println(getTitle(document))
            val nav = content.select(".chapter-nav")
            for (paragraph in content.reversed().iterator()) {
                content.remove(paragraph)
                if (paragraph.tagName() == "hr") {
                    break
                }
            }
//            println(content)
//            println(nav)
            var prev: String? = null
            var next: String? = null
            if (nav.size == 1) {
                if (nav[0].text() == "Previous Chapter") {
                    prev = nav[0].attr("href")
                } else {
                    next = nav[0].attr("href")
                }
            } else if (nav.size == 2) {
                prev = nav[0].attr("href")
                next = nav[1].attr("href")
            }
            println(content.map{ it.text() })
//            println(ObservableFromIterable(content).map { it.text() }.toList().blockingGet())
//            println(Chapter(1, 2.toFloat(), "title", "url", 3, prev, next))
        }
    }

    @Test
    fun getBookListFromTag() {
        val body = api
            .getList("korean")
            .blockingGet()

        val document = Parser.parse(body.string(), WUXIA_URL) as Element
        val bookList = mutableListOf<WBook>()
        for (book in document.select(".media")) {
            println("---------------------------------------------------")
            if (book.select(".media-left").first() == null) {
                continue
            }
            val mediaLeft = book.select(".media-left").first().child(0)
            val url = mediaLeft.attr("href")
            val image = mediaLeft.child(0).attr("src")
            val nodes = book.select(".media-body").first().children()
            val header = nodes.removeAt(0)
            val title = header.children().last().text()
            val synp = nodes.map { it.text() }.joinToString("\n")
//            val desc = getBookDescription(url).joinToString("\n")
            val desc = "desc"
            val b = WBook(url, image, title, synp, desc)
            bookList.add(b)
        }
        println(bookList)
    }

    data class WBook(val url: String,
                     val imageUrl: String,
                     val title: String,
                     val synposis: String,
                     val description: String,
                     val id:Int = title.hashCode())
    data class WChapter(val url: String, val title: String)

    @Test
    fun getChapterList() {
        getChapterList("/novel/praise-the-orc")
    }

    fun getChapterList(url: String) {
        val body = api.getUrl(url).blockingGet().string()
        val document = Parser.parse(body, WUXIA_URL)
        val links = document.select(".panel-group").select("a[href]")
            .filter { it.attr("href").startsWith("/novel"); }
            .map { WChapter(it.attr("href"), it.text()) }
        println(links)
    }

    fun getBookDescription(url: String): List<String> {
        val body = api.getUrl(url).blockingGet().string()
        val document = Parser.parse(body, WUXIA_URL)
        return document.select(".fr-view").map { it.text() }
    }

    fun getNav(document: Document) {
        val content = document.select(".fr-view").first().children()
        println(content)
        println("<><><>")
        println(content.select(":root > hr"))
    }

    fun getNav(document: Element) {
        val content = document.select(".fr-view").first().children()
        println(content)
        println("<><><>")
        println(content.select(":root > hr"))
    }

    /**
     * Get Title of page from HTML
     */
    fun getTitle(document: Document) =
        document.select("meta[property=og:title]").attr("content")

}