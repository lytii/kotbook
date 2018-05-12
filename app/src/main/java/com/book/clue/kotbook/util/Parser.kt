package com.book.clue.kotbook.util

import android.util.Log
import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.Chapter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import kotlin.collections.ArrayList

class Parser {
    companion object {
        fun parseWBookList(document: Document): MutableList<Book> {
            val bookList = mutableListOf<Book>()
            for (book in document.select(".media")) {
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
                val b = Book(url.hashCode(), title, image, url, synp, desc)
                bookList.add(b)
            }
            return bookList
        }

        fun parseWChapterList(document: Document, bookId: Int): List<Chapter> {
            var i = 0
            return document
                .select(".panel-group")
                .select("a[href]")
                .filter { it.attr("href").startsWith("/novel"); }
                .map {
                    Chapter(i++,
                        i.toFloat(),
                        it.text(),
                        it.attr("href"),
                        bookId, "", "")
                }
        }

        fun parseWChapter(document: Document, chapter: Chapter): List<String> {
            val content = document.select(".fr-view").first().children()
            val nav = content.select(".chapter-nav")
            for (paragraph in content.reversed().iterator()) {
                content.remove(paragraph)
                if (paragraph.tagName() == "hr") {
                    break
                }
            }

            for (paragraph in content.reversed().iterator()) {
                if (paragraph.childNodeSize() == 0) {
                    Log.e("Parser", "Removing $paragraph")
                    content.remove(paragraph)
                }
            }
            var prevUrl: String? = null
            var nextUrl: String? = null
            if (nav.size == 1) {
                val text = nav[0].text()
                if (text == "Previous Chapter") {
                    prevUrl = nav[0].attr("href")
                } else if (text == "Next Chapter") {
                    nextUrl = nav[0].attr("href")
                }
            } else if (nav.size == 2) {
                prevUrl = nav[0].attr("href")
                nextUrl = nav[1].attr("href")
            }
            chapter.nextUrl = nextUrl
            chapter.prevUrl = prevUrl
            return content.map { it.text()}
        }

        fun parseForBookList(toParse: Document): ArrayList<Book> {
            val list = toParse.select("[class=sub-menu]").select("[href]")
            val linkList = ArrayList<Book>()
            for (item in list) {
                var text = item.text()
                if (text.equals("About Us")) break
                text = if (text.startsWith("[KR]") || text.startsWith("[EN]"))
                    text.substring(5) else text
                val index = text.indexOf('(')
                if (index > 0) {
                    text = text.substring(0, index)
                }
//                linkList.add(Book(text, item.attr("href")))
            }
            return linkList
        }

        fun parseForChapterList(toParse: Document): ArrayList<Book> {
            val chapters = toParse.select("[itemprop=articleBody]").select("[href]")
            val linkList = ArrayList<Book>()
            for (chapter in chapters) {
                var text = chapter.text()
                val chapterSubIndex = text.indexOf("Chapter ")
                if (chapterSubIndex > 0) {
                    text = text.substring(chapterSubIndex + 8)
                }
//                linkList.add(Book(text, chapter.attr("href")))
            }
            return linkList
        }

        fun parseForChapter(toParse: Document): MutableList<String> {
            val list = toParse.select("div[id=chapterContent]")
                    .select("p")
                    .filter { it.text() != "" }
                    .map { it.text() }
                    .toMutableList()

            val nav = toParse.select("div[class=btn-group btn-group-justified chapter-navigation]").select("a[class=btn btn-lg btn-link]")
            list.add(nav[2].attr("href").toString())
            list.add(nav[0].attr("href").toString())
            list.add(toParse.select("title").toString())
            return list
        }

        fun parseForWuxiaChapter(toParse: Document): java.util.ArrayList<String> {
            // parse response to get chapter paragraph
            var chapterContent = toParse.select("div#chapterContent")
            if (chapterContent.size == 0)
            // backup parsing
                chapterContent = toParse.select("div [itemprop='articleBody']")
//            println(chapterContent)
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
            var links = chapterContent.select("[href]")
            if (links.size == 0) {
                chapterContent = toParse.select("div [itemprop='articleBody']")
                links = chapterContent.select("[href]")
            }
            paragraphs.add(paragraphs.size, links[1].attr("href"))
            paragraphs.add(paragraphs.size, links[0].attr("href"))
            return paragraphs
        }

        private fun removeBlanks(nodes: List<Node>): java.util.ArrayList<String> {
            val paragraphs = java.util.ArrayList<String>()
            var first = false // to remove multiple new lines in a row
            for (node in nodes) {
                val string = node.toString().replace("<p[^>]+>|</p>|<p>".toRegex(), "")
                if (string != " " && string != "<hr>" && string != "<br>" && string != "&nbsp;") {
                    if (string.isEmpty() && first) {
                        // second new line, don't add it
                        first = false
                    } else {
                        // first new line usually has 'some' meaning
                        first = string.isEmpty()
                        paragraphs.add(string)
                    }
                }
            }
            return paragraphs
        }
    }
}