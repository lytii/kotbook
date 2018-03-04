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
import com.book.clue.kotbook.booklist.ChapterAdapter
import com.book.clue.kotbook.dagger.NetworkComponent
import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.db.ChapterParagraph
import com.book.clue.kotbook.util.Network
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter.view.*
import java.io.Serializable
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChapterController(args: Bundle) : Controller() {
    lateinit var paragraphListView: RecyclerView
    lateinit var navNextButton: Button
    lateinit var navPrevButton: Button
    lateinit var navBar: LinearLayout

    @Inject
    lateinit var network: Network
    var fullscreen = true
    val chapter: Chapter = args.getSerializable(CHAPTER_KEY) as Chapter
    val paragraphList: ArrayList<String> = args.getStringArrayList(PARAGRAPH_LIST_KEY)

    companion object {
        val CHAPTER_KEY = "ChapterController.Chapter"
        val PARAGRAPH_LIST_KEY = "ChapterController.ParagraphList"
    }

    constructor(chapterParagraph: ChapterParagraph) : this(
            BundleBuilder(Bundle())
                    .putChapter(chapterParagraph.chapter)
                    .putParagraphs(chapterParagraph.chapterParagraphs)
                    .build()
    )
    class BundleBuilder(val args: Bundle) {
        fun putChapter(chapter: Chapter): BundleBuilder {
            args.putSerializable(CHAPTER_KEY, chapter as Serializable)
            return this
        }
        fun putParagraphs(paragraphList: List<String>): BundleBuilder {
            args.putStringArrayList(PARAGRAPH_LIST_KEY, paragraphList as ArrayList<String>)
            return this
        }

        fun build() = args
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        NetworkComponent.get().
                inject(this)
        val view = inflater.inflate(R.layout.activity_chapter, container, false)

        navBar = view.nav_bar
        navNextButton = view.nav_next
        navPrevButton = view.nav_prev
        paragraphListView = view.paragraph_list
        paragraphListView.layoutManager = LinearLayoutManager(inflater.context)
        showChapter(paragraphList)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = chapter.title
    }

//    private fun getChapter(chapterUrl: String) {
//        this.chapterUrl = chapterUrl
//        showLoading(true)
//        network.getChapter(chapterUrl)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally { showLoading(false) }
//                .subscribe(this::showChapter)
//    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) view?.chapter_loading_progress_bar?.visibility = View.VISIBLE
        else view?.chapter_loading_progress_bar?.visibility = View.GONE
    }

    private fun showChapter(paragraphList: MutableList<String>) {
//        title = paragraphList.removeAt(0)

//        title = paragraphList.removeAt(paragraphList.lastIndex)
//        var titleStart = title.indexOf("Chapter")
//        var titleEnd = title.lastIndexOf("-")
//        title = title.substring(titleStart, titleEnd)
//        activity?.actionBar?.title = title
//        paragraphList.add(0, title.split("-")[1])
//        paragraphList.add(1, "-----------------------------")

//        val prev = paragraphList.removeAt(paragraphList.lastIndex)
//        val next = paragraphList.removeAt(paragraphList.lastIndex)
//        navPrevButton.setOnClickListener { getChapter(prev) }
//        navNextButton.setOnClickListener { getChapter(next) }

//        val last = paragraphList[paragraphList.size - 1]
//        if (last.contains("href")) {
//            paragraphList.removeAt((paragraphList.size - 1))
//        }
        paragraphListView.adapter = ChapterAdapter(paragraphList, this::toggleFullScreen)


        Observable.just(true)
                .delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::setFullScreen)
                .subscribe()

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