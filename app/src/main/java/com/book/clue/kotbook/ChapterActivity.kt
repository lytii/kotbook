package com.book.clue.kotbook

import android.app.Activity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        view = paragraph_list
        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val intent = intent
        val url = intent.getStringExtra(ChapterListActivity.EXTRA_CHAPTER_URL)
        getChapter(url)
    }

    fun getChapter(url: String) {
        network.getChapter(url, this::setChapter)
    }

    fun setChapter(paragraphList: ArrayList<String>) {
        view.adapter = ChapterAdapter(paragraphList)
    }
}
