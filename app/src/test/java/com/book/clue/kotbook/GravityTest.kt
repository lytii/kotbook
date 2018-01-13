package com.book.clue.kotbook

import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.util.Network
import com.book.clue.kotbook.util.Parser
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.Test

class GravityTest {

    @Test
    fun testGravityTales() {
        val network = Network()
        val list = network.bookNetwork
                .getFromUrl("http://gravitytales.com/")
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { it.select("a[href]") }
                .map { list -> list.filter { it.attr("href").startsWith("/novel") } }
                .map { it.sortedBy { it.attr("href") } }
                .blockingGet()
        listOf(1, 2).sorted()
//        network.getBookList()
//                .subscribe { list -> abc(list)  }
    }

    @Test
    fun network() {
//        var baseUrl = "http://gravitytales.com/"
        val list = Network()
                .getBookList()
//                .bookNetwork.getFromUrl(baseUrl)
//                .map(ResponseBody::string)
//                .map(Jsoup::parse)
//                .map { Parser.parseForBookList(it) }
//                .map(Collection<Book>::sorted)
                .blockingGet()
        print(list)
    }

    @Test
    fun chap() {
        val chap = Network()
                .getChapter("http://gravitytales.com/Novel/mmorpg-rebirth-of-the-legendary-guardian/rlg-chapter-1")
                .blockingGet()
        print(chap)
    }


}