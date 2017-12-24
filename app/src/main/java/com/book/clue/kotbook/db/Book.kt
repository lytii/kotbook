package com.book.clue.kotbook.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Book (var bookTitle: String = "NO_TITLE", var url: String = "")
    : Comparable<Book> {
    override fun compareTo(other: Book): Int {
        return bookTitle.compareTo(other.bookTitle)
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
