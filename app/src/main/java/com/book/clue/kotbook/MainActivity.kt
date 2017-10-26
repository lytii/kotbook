package com.book.clue.kotbook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.book.clue.kotbook.BookList.BookListAdapater
import com.book.clue.kotbook.BookList.BookListItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    val bookList = listOf<BookListItem>(
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false),
            BookListItem(bookTitle = "Favorite Book", url = "favorite.com", isFavorite = true),
            BookListItem(bookTitle = "Other Book", url = "other.com", isFavorite = false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val booklistAdapter = BookListAdapater(bookList, this::toast)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycler_view.adapter = booklistAdapter
    }
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
