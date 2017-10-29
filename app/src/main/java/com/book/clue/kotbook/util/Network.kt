package com.book.clue.kotbook.util

import com.book.clue.kotbook.booklist.BookListItem
import com.book.clue.kotbook.booklist.BookNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

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

    fun getBookList(listener: (List<BookListItem>) -> Unit) {
        bookNetwork.getFromUrl(WUXIA_URL)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { parseForBookList(it) }
                .map(Collection<BookListItem>::sorted)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener)
    }

    fun getChapterList(url: String, listener: (ArrayList<BookListItem>) -> Unit) {
        bookNetwork.getFromUrl(url)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { parseForChapterList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener)
    }

    fun getChapter(url: String, listener: (ArrayList<String>) -> Unit) {
        bookNetwork.getFromUrl(url)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map { parseForChapter(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener)
    }

    fun parseForBookList(toParse: Document): ArrayList<BookListItem> {
        val list = toParse.select("[class=sub-menu]").select("[href]")
        val linkList = ArrayList<BookListItem>()
        for (item in list) {
            var text = item.text()
            if (text.equals("About Us")) break
            text = if (text.startsWith("[KR]")) text.substring(5) else text
            val index = text.indexOf('(')
            if (index > 0) {
                text = text.substring(0, index)
            }
            val MAX_TITLE_LENGTH = 32
            if (text.length > MAX_TITLE_LENGTH) {
                text = "${text.substring(0, MAX_TITLE_LENGTH - 2)}.."
            }
            linkList.add(BookListItem(text, item.attr("href")))
        }
        return linkList
    }

    fun parseForChapterList(toParse: Document): ArrayList<BookListItem> {
        val chapters = toParse.select("[itemprop=articleBody]").select("[href]")
        val linkList = ArrayList<BookListItem>()
        for (chapter in chapters) {
            linkList.add(BookListItem(chapter.text(), chapter.attr("href")))

        }
        return linkList
    }

    @Throws(IOException::class)
    fun parseForChapter(toParse: Document): java.util.ArrayList<String> {
        // parse response to get chapter text
        var chapterContent = toParse.select("div#chapterContent")
        if (chapterContent.size == 0)
        // backup parsing
            chapterContent = toParse.select("div [itemprop='articleBody']")
        println(chapterContent)
        val all = chapterContent[0].childNodes()

        val paragraphs = removeBlanks(all)

        // remove if first line is `Next Chapter Previous Chapter`
        if (paragraphs.get(0).contains("Next Chapter") || paragraphs.get(0).contains("Previous Chapter")) {
            val title = Jsoup.parse(paragraphs.get(0))
            val spanSelect = title.select("span")
            if (spanSelect.size == 2) {
                paragraphs.set(0, spanSelect[1].toString())
            } else {
                paragraphs.removeAt(0)
            }
        }

        // add title as first
        paragraphs.add(0, toParse.select("meta[property='og:title']").attr("content"))

        // add prev/next links to 0,1
        val links = chapterContent.select("[href]")
        if (links.size > 0) {
            paragraphs.add(paragraphs.size, links[1].attr("href"))
            paragraphs.add(paragraphs.size, links[0].attr("href"))
        }

        return paragraphs
    }

    fun removeBlanks(nodes: List<Node>): java.util.ArrayList<String> {
        val paragraphs = java.util.ArrayList<String>()
        var first = false // to remove multiple new lines in a row
        for (node in nodes) {
            val string = node.toString().replace("<p[^>]+>|</p>|<p>".toRegex(), "")
            if (string != " "
                    && string != "<hr>"
                    && string != "<br>"
                    && string != "&nbsp;") {
                if (string.length == 0 && first) {
                    // second new line, don't add it
                    first = false
                } else {
                    // first new line usually has 'some' meaning
                    first = string.length == 0
                    paragraphs.add(string.replace("&nbsp;".toRegex(), ""))
                }
            }
        }
        return paragraphs
    }
}