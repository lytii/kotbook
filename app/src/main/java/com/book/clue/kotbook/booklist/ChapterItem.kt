package com.book.clue.kotbook.booklist

data class ChapterItem(
        var paragraphText: ArrayList<String>,
        var chapterTitle: String,
        var chapterUrl: String
) : Comparable<ChapterItem> {
    override fun compareTo(other: ChapterItem): Int {
        return chapterUrl.compareTo(other.chapterUrl)
    }
}