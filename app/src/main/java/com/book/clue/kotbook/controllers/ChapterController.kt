package com.book.clue.kotbook.controllers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import com.bluelinelabs.conductor.Controller
import com.book.clue.kotbook.R
import com.book.clue.kotbook.booklist.BundleBuilder
import com.book.clue.kotbook.booklist.ChapterAdapter
import com.book.clue.kotbook.util.DaggerNetworkComponent
import com.book.clue.kotbook.util.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_chapter.view.*
import javax.inject.Inject

class ChapterController(args: Bundle) : Controller() {
    lateinit var paragraphListView: RecyclerView
    lateinit var navNextButton: Button
    lateinit var navPrevButton: Button
    lateinit var navBar: LinearLayout

    @Inject
    lateinit var network: Network
    var fullscreen = true
    var chapterUrl = args.getString(CHAPTER_URL_KEY)
    var title = args.getString(TITLE_KEY)
    var chapter: ArrayList<String> = ArrayList()

    companion object {
        val CHAPTER_URL_KEY = "ChapterController.ChapterUrl"
        val TITLE_KEY = "ChapterController.Title"
    }

    constructor(title: String, url: String) : this(
            BundleBuilder(Bundle())
                    .saveChapterState(title, url)
                    .build()
    )

    constructor(chapter: ArrayList<String>) : this(
            BundleBuilder(Bundle()).build()
    ) {
        this.chapter = chapter
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        DaggerNetworkComponent.builder().build().inject(this)

        val view = inflater.inflate(R.layout.activity_chapter, container, false)
        navBar = view.nav_bar
        navNextButton = view.nav_next
        navPrevButton = view.nav_prev
        paragraphListView = view.paragraph_list
        paragraphListView.layoutManager = LinearLayoutManager(inflater.context)

        if (chapter.isEmpty()) {
            getChapter(chapterUrl)
        } else {
            showChapter(chapter)
        }
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = title
    }

    private fun getChapter(chapterUrl: String) {
        this.chapterUrl = chapterUrl
        showLoading(true)
        network.getChapter(chapterUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { showLoading(false) }
                .subscribe(this::showChapter)
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) view?.chapter_loading_progress_bar?.visibility = View.VISIBLE
        else view?.chapter_loading_progress_bar?.visibility = View.GONE
    }

    private fun showChapter(paragraphList: ArrayList<String>) {
        title = paragraphList.removeAt(0)
        activity?.actionBar?.title = title

        val size = paragraphList.size
        navPrevButton.setOnClickListener { getChapter(paragraphList.removeAt(size - 1)) }
        navNextButton.setOnClickListener { getChapter(paragraphList.removeAt(size - 2)) }

        val last = paragraphList.get(paragraphList.size - 1)
        if (last.contains("href")) {
            paragraphList.removeAt((paragraphList.size - 1))
        }
        paragraphListView.adapter = ChapterAdapter(paragraphList, this::toggleFullScreen)


//        Observable.just(true)
//                .delay(750, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(this::setFullScreen)
//                .subscribe()

    }

    private fun toggleFullScreen() {
        setFullScreen(fullscreen)
    }

    private fun setFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            activity?.actionBar?.hide()
            navBar.visibility = View.GONE
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            activity?.actionBar?.show()
            navBar.visibility = View.VISIBLE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        fullscreen = !isFullScreen
    }

    override fun handleBack(): Boolean {
        setFullScreen(false)
        return super.handleBack()
    }
}