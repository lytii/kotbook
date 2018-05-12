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
import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.db.ChapterParagraph
import com.book.clue.kotbook.managers.BookListManager
import com.book.clue.kotbook.managers.SharedPrefsManager
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

    @Inject
    lateinit var bookListManager: BookListManager

    @Inject
    lateinit var sharedPrefsManager: SharedPrefsManager

    var fullscreen = true
    var chapter: Chapter = args.getSerializable(CHAPTER_KEY) as Chapter
    var chapterId: Int = chapter.id
    var paragraphList: ArrayList<String> = args.getStringArrayList(PARAGRAPH_LIST_KEY)

    companion object {
        val CHAPTER_KEY = "ChapterController.Chapter"
        val PARAGRAPH_LIST_KEY = "ChapterController.ParagraphList"
    }

    constructor(chapterParagraph: ChapterParagraph) : this(
            BundleBuilder(Bundle())
                    .putChapter(chapterParagraph.chapter)
                    .putParagraphs(chapterParagraph.paragraphs)
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
        NetworkComponent.get().inject(this)
        val view = inflater.inflate(R.layout.activity_chapter, container, false)

        navBar = view.nav_bar
        navNextButton = view.nav_next
        navPrevButton = view.nav_prev
        paragraphListView = view.paragraph_list
        paragraphListView.layoutManager = LinearLayoutManager(inflater.context)
        showChapter()
        return view
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) view?.chapter_loading_progress_bar?.visibility = View.VISIBLE
        else view?.chapter_loading_progress_bar?.visibility = View.GONE
    }

    private fun showChapter() {
        activity?.actionBar?.title = chapter.title
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
        val prevUrl = chapter.prevUrl
        if (prevUrl != null) {
            navPrevButton.setOnClickListener { getChapter(prevUrl) }
        }
        val nextUrl = chapter.nextUrl
        if (nextUrl != null) {
            navNextButton.setOnClickListener { getChapter(nextUrl) }
        }

//        navNextButton.setOnClickListener { getChapter(next) }

//        val last = paragraphList[paragraphList.size - 1]
//        if (last.contains("href")) {
//            paragraphList.removeAt((paragraphList.size - 1))
//        }
        paragraphListView.adapter = ChapterAdapter(paragraphList, this::toggleFullScreen)


//        Observable.just(true)
//                .delay(750, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(this::setFullScreen)
//                .subscribe()

    }

    private fun getChapter(url: String) {
        showLoading(true)
        bookListManager.getChapterByUrl(url)
            .map {
                run {
                    chapter = it.chapter
                    args.putSerializable(CHAPTER_KEY, chapter)
                    paragraphList = it.paragraphs as ArrayList<String>
                    args.putStringArrayList(PARAGRAPH_LIST_KEY,
                        paragraphList)
                }
            }
            .toCompletable()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { showLoading(false) }
            .doOnComplete { sharedPrefsManager.saveChapterId(chapter.id) }
            .subscribe { showChapter() }
    }

    private fun getChapter(chapterNumber: Float) {
        showLoading(true)
        bookListManager.getChapterByNumber(chapterNumber, chapter.bookId)
                .map {
                    run {
                        chapter = it.chapter
                        args.putSerializable(CHAPTER_KEY, chapter)
                        paragraphList = it.paragraphs as ArrayList<String>
                        args.putStringArrayList(PARAGRAPH_LIST_KEY,
                                paragraphList)
                    }
                }
                .toCompletable()
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { showLoading(false) }
                .doOnComplete { sharedPrefsManager.saveChapterId(chapter.id) }
                .subscribe { showChapter() }
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