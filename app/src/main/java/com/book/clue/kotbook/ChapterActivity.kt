package com.book.clue.kotbook

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.book.clue.kotbook.booklist.ChapterAdapter
import com.book.clue.kotbook.util.Network
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter.*
import java.util.concurrent.TimeUnit

class ChapterActivity : Activity() {
    lateinit var view: RecyclerView
    val network = Network()
    lateinit var sharedPrefs: SharedPreferences
    var fullscreen = true

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

    fun toggleFullScreen() {
        setFullScreen(fullscreen)
    }

    fun setFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            actionBar.hide()
            nav_bar.visibility = View.GONE
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            actionBar.show()
            nav_bar.visibility = View.VISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        fullscreen = !isFullScreen
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
        view.adapter = ChapterAdapter(paragraphList, this::toggleFullScreen)
        Observable.just(true).delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::setFullScreen)
                .subscribe()
    }
}
