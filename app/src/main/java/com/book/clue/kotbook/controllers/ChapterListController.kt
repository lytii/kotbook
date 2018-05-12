package com.book.clue.kotbook.controllers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.book.clue.kotbook.R
import com.book.clue.kotbook.booklist.ChapterListAdapter
import com.book.clue.kotbook.dagger.NetworkComponent
import com.book.clue.kotbook.db.Book
import com.book.clue.kotbook.db.Chapter
import com.book.clue.kotbook.db.ChapterParagraph
import com.book.clue.kotbook.managers.BookListManager
import com.book.clue.kotbook.managers.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter_list.view.*
import java.io.Serializable
import javax.inject.Inject


class ChapterListController(args: Bundle) : Controller() {

    @Inject
    lateinit var bookListManager: BookListManager
    @Inject
    lateinit var sharedPrefsManager: SharedPrefsManager

    lateinit var chapterListView: RecyclerView
    val book: Book = args.getSerializable(BOOK_KEY) as Book

    companion object {
        val BOOK_KEY = "ChapterListController.BOOK"
    }

    constructor(book: Book) : this(
            BundleBuilder(
                    Bundle())
                    .putBook(book)
                    .build()
    )

    class BundleBuilder(val args: Bundle) {
        fun putBook(book: Book): BundleBuilder {
            args.putSerializable(BOOK_KEY, book as Serializable)
            return this
        }

        fun build() = args
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        NetworkComponent.get().inject(this)
        val view = inflater.inflate(R.layout.activity_chapter_list, container, false)
        chapterListView = view.chapter_list
        chapterListView.layoutManager = LinearLayoutManager(inflater.context)
        getChapterList()
        return view
    }

    override fun onAttach(view: View) {
        val id = sharedPrefsManager.getCurrentChapterId()
        if (id != -1) {
            bookListManager.getChapterById(id)
                .subscribeOn(Schedulers.io())
                .map { chapter ->
                    view.last_seen_button.setOnClickListener { getChapter(chapter) }
                    view.last_seen_button.text = chapter.title
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
        super.onAttach(view)
        activity?.actionBar?.title = book.name
    }

    private fun getChapterList() {
        bookListManager
        .getChapterList(book)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showChapterList)
    }

    private fun showChapterList(chapterList: List<Chapter>) {
        chapterListView.adapter = ChapterListAdapter(chapterList, this::getChapter)
    }

    private fun getChapter(chapter: Chapter) {
        sharedPrefsManager.saveChapterId(chapter.id)
        view?.loading_progress_bar?.visibility = View.VISIBLE
        bookListManager.getChapter(chapter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayChapter)
    }

    private fun displayChapter(chapter: ChapterParagraph) {
        view?.loading_progress_bar?.visibility = View.GONE
        router.pushController(
                RouterTransaction.with(ChapterController(chapter))
                        .pushChangeHandler(VerticalChangeHandler())
                        .popChangeHandler(VerticalChangeHandler())
        )
    }
}