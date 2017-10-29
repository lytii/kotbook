package com.book.clue.kotbook

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.book.clue.kotbook.booklist.ChapterAdapter
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_chapter.*

class ChapterActivity : Activity() {
    lateinit var view: RecyclerView
    val network = Network()
    lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)


        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)
        view = paragraph_list
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val intent = intent
        val url = intent.getStringExtra(ChapterListActivity.EXTRA_CHAPTER_URL)
        getChapter(url)
    }

    fun getChapter(url: String) {
        sharedPrefs.edit().putString(getString(R.string.chapter_url_key), url).apply()
        network.getChapter(url, this::setChapter)
    }

    fun setChapter(paragraphList: ArrayList<String>) {
        title = paragraphList.removeAt(0)

        val size = paragraphList.size

        val prev_link = paragraphList.removeAt(size - 1)
        nav_prev.setOnClickListener { getChapter(prev_link) }
        val next_link = paragraphList.removeAt(size - 2)
        nav_next.setOnClickListener { getChapter(next_link) }

        val last = paragraphList.get(paragraphList.size - 1)
        if (last.contains("href")) {
            paragraphList.removeAt((paragraphList.size - 1))
        }
        view.adapter = ChapterAdapter(paragraphList)
    }
}
