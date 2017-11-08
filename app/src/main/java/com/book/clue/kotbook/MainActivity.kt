package com.book.clue.kotbook

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.book.clue.kotbook.controllers.BookListController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    companion object {
//        val EXTRA_BOOK_URL = "com.book.clue.kotbook.bookUrl"
//        val EXTRA_RELAUNCH = "com.book.clue.kotbook.relaunch"
//        val EXTRA_CHAPTER_URL = "com.book.clue.kotbook.chapterUrl"
    }

//    lateinit var view: RecyclerView
//    val network = Network()
//    var relaunch = false
//    lateinit var sharedPrefs: SharedPreferences
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = controller_container as ViewGroup
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(BookListController()))
        }
//        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE)
//
//        val bookUrl = sharedPrefs.getString(getString(R.string.book_url_key), null)
//        if (bookUrl != null) {
//            relaunch = true
//            showChapterList(bookUrl)
//            return
//        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
//    override fun onStart() {
//        super.onStart()
//        title = "Book List"
//        view = recycler_view
//        network.getBookList(this::setBookList)
//        view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//    }
//
//    fun setBookList(bookList: List<BookListItem>) {
//        val booklistAdapter = BookListAdapter(bookList, this::showChapterList)
//        view.adapter = booklistAdapter
//    }
//
//    fun showChapterList(url: String) {
//        val intent = Intent(this, ChapterListActivity::class.java)
//        if (relaunch) {
//            val chapterUrl = sharedPrefs.getString(getString(R.string.chapter_url_key), null)
//            intent.putExtra(EXTRA_CHAPTER_URL, chapterUrl)
//            intent.putExtra(EXTRA_RELAUNCH, true)
//            relaunch = false
//        }
//        intent.putExtra(EXTRA_BOOK_URL, url)
//        startActivity(intent)
//    }
}
