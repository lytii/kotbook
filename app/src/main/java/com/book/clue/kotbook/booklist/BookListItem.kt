package com.book.clue.kotbook.booklist

data class BookListItem(
        val bookTitle: String,
        val url: String = "",
        val isFavorite: Boolean = false
) : Comparable<BookListItem> {
    override fun compareTo(other: BookListItem): Int {
        return bookTitle.compareTo(other.bookTitle)
    }
}
