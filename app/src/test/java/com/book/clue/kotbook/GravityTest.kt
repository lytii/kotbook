package com.book.clue.kotbook

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.util.ChapterContent
import com.book.clue.kotbook.util.Network
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.junit.Test

class GravityTest {

    @Test
    fun bookApi() {
//        val coverBase = "https://cdn.gravitytales.com/images/covers/"
        val myString = Network().getBookListN()
                .blockingGet()
        print(myString)
//                .map(ResponseBody::string)
//                .map { Gson().fromJson(it, Array<Book>::class.java).asList() }
//                .blockingGet()
//        print(myString)
    }

    @Test
    fun groupsApi() {
        val myString: List<Book> = Network().getBookListN().blockingGet()
        val book: Book = myString.find { it.name.contains("Guardian", true) }
                ?: Book(1, "", "", "")

        val chapterGroup = Network().getChapterList(book.id)
                .blockingGet()
        chapterGroup.forEach { println("C: $it") }

        Observable
                .just(1, 2, 3)
                .flatMap { Observable.just(it * 10, it * 100) }
                .subscribe { println("D: $it") }
    }

    @Test
    fun chapterApi() {
        val chapterId = 22328
        val string = Network().wBookApi.getChapter(chapterId)
                .map(ResponseBody::string)
                .blockingFirst()
        val c = Gson().fromJson(string, ChapterContent::class.java)
        val text = Jsoup.parseBodyFragment(c.content)
                .body()
                .select("p")
                .map { it.text() }
                .filter { it != "" }
        println("E: $text")
    }
}