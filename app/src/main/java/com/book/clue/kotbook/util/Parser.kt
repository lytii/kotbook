package com.book.clue.kotbook.util

import com.book.clue.kotbook.db.Book
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import java.util.*
import kotlin.collections.ArrayList

class Parser {
    companion object {
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
                linkList.add(Book(text, item.attr("href")))
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
                linkList.add(Book(text, chapter.attr("href")))
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