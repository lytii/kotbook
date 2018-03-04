package com.book.clue.kotbook.managers

import android.util.Log
import com.book.clue.kotbook.db.*
import com.book.clue.kotbook.util.Network
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookListManager @Inject constructor(val bookDao: BookDao,
                                          val chapterDao: ChapterDao,
                                          val network: Network) {

    fun getAllBooks() = getBooks()

    fun getBooks() =
            bookDao.getAllBooks()
                    .subscribeOn(Schedulers.io())
                    .flatMap { list ->
                        if (list.isNotEmpty()) {
                            Log.d("Book", "loading from db")
                            Single.just(list)
                        } else {
                            Log.d("Book", "loading from network")
                            getFromNetwork()
                        }
                    }
                    .map { it.sorted() }

    fun getFromNetwork() =
            network.getGravityBooklist()
                    .doOnSuccess { addAllBooks(it) }

    fun getChapterList(bookId: Int): Single<List<Chapter>> =
            chapterDao.getChapterList(bookId)
                    .subscribeOn(Schedulers.io())
                    .flatMap { list ->
                        if (list.isEmpty()) {
                            Log.d("Chapter", "loading from network")
                            getChapterListFromNetwork(bookId)
                        } else {
                            Log.d("Chapter", "loading from db")
                            Single.just(list)
                        }
                    }
                    .doOnError { Log.e("", it.toString()) }

    fun getChapter(chapterId: Int): Single<ChapterParagraph> =
            chapterDao.getChapterById(chapterId)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        if (it.chapterParagraphs.isEmpty()) {
                            getChapterFromNetwork(chapterId)
                        } else {
                            Single.just(it)
                        }
                    }


    private fun addAllBooks(bookList: List<Book>): Unit =
            bookDao.addAllBooks(*bookList.toTypedArray())

    private fun getChapterListFromNetwork(bookId: Int): Single<List<Chapter>> =
            network.getChapterList(bookId)
                    .doOnSuccess { addChapters(it) }

    private fun addChapters(chapterList: List<Chapter>) {
        Log.d("Chapter", "adding chapters : $chapterList")
        chapterDao.addChapters(*chapterList.toTypedArray())
    }

    private fun getChapterFromNetwork(chapterId: Int) =
            network.getChapter(chapterId)
                    .map {
                        it.forEachIndexed { index, paragraph ->
                            chapterDao.addParagraph(
                                    Paragraph(chapterId * 1000 + index, chapterId, index, paragraph))
                        }
                    }
                    .flatMap { chapterDao.getChapterById(chapterId) }
}
