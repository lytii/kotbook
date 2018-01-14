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
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.book.clue.kotbook.MainActivity
import com.book.clue.kotbook.WebActivity
import com.book.clue.kotbook.util.CHAPTER_LIST_WEB_REQUEST


class ChapterListController(args: Bundle) : Controller() {

    @Inject
    lateinit var network: Network

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
//        network.getChapterList(bookUrl)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::showChapterList)
        showWeb()
        return view
    }

    fun showWeb() {
        val b = Bundle()
        b.putString("bookUrl", bookUrl)
        var intent = Intent(activity, WebActivity::class.java)
        intent.putExtras(b)
        startActivityForResult(intent, CHAPTER_LIST_WEB_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val url = data?.extras?.get(activity?.getString(R.string.bookUrl)).toString() ?: "404"
        router.pushController(RouterTransaction.with(ChapterController("Chapter", url)))
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun displayChapter(chapter: MutableList<String>) {
        view?.loading_progress_bar?.visibility = View.GONE
        router.pushController(
                RouterTransaction.with(ChapterController(chapter))
                        .pushChangeHandler(VerticalChangeHandler())
                        .popChangeHandler(VerticalChangeHandler())
        )
    }
}