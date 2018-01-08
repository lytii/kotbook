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
import com.book.clue.kotbook.util.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_book_list.view.*
import javax.inject.Inject

class ChapterListController(args: Bundle) : Controller() {
    @Inject lateinit var network: Network
    lateinit var chapterListView: RecyclerView
    val bookUrl: String = args.getString(BOOK_URL_KEY)
    val title: String = args.getString(TITLE_KEY)

    companion object {
        val BOOK_URL_KEY = "ChapterListController.BookUrl"
        val TITLE_KEY = "ChapterListController.Title"
    }

    constructor(title: String, url: String) : this(
            BundleBuilder(Bundle()).putString(BOOK_URL_KEY, url)
                    .putString(TITLE_KEY, title)
                    .build())

    class BundleBuilder(val args: Bundle) {
        fun putString(url_key: String, url: String): BundleBuilder {
            args.putString(url_key, url)
            return this
        }

        fun build() = args
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        NetworkComponent.get().inject(this)
        val view = inflater.inflate(R.layout.activity_book_list, container, false)
        chapterListView = view.book_list
        chapterListView.layoutManager = LinearLayoutManager(inflater.context)
        network.getChapterList(bookUrl, this::showChapterList)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        activity?.actionBar?.title = title
    }

    fun showChapterList(chapterList: List<Book>) {
        chapterListView.adapter = ChapterListAdapter(chapterList, this::getChapter)
    }

    private fun getChapter(chapterUrl: String) {
        view?.loading_progress_bar?.visibility = View.VISIBLE
        network.getChapter(chapterUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayChapter)
    }

    private fun displayChapter(chapter: ArrayList<String>) {
        view?.loading_progress_bar?.visibility = View.GONE
        router.pushController(
                RouterTransaction.with(ChapterController(chapter))
                        .pushChangeHandler(VerticalChangeHandler())
                        .popChangeHandler(VerticalChangeHandler())
        )
    }
}