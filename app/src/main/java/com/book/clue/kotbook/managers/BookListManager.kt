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
            network.getWuxiaBookList()
                    .doOnSuccess { addAllBooks(it) }

    fun getChapterList(book: Book): Single<List<Chapter>> =
            chapterDao.getChapterList(book.id)
                    .subscribeOn(Schedulers.io())
                    .flatMap { list ->
                        if (list.isEmpty()) {
                            Log.d("Chapter", "loading from network")
                            getChapterListFromNetwork(book)
                        } else {
                            Log.d("Chapter", "loading from db")
                            Single.just(list.sortedBy { it.number })
                        }
                    }
                    .doOnError { Log.e("", it.toString()) }

    fun getChapter(chapter: Chapter): Single<ChapterParagraph> =
            chapterDao.getChapterById(chapter.id)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        if (it.paragraphs.isEmpty()) {
                            getChapterFromNetwork(chapter)
                        } else {
                            Single.just(it)
                        }
                    }
    fun getChapterByUrl(url: String): Single<ChapterParagraph> {
        return chapterDao.getChapterByUrl(url)
            .subscribeOn(Schedulers.io())
            .flatMap {
                if (it.paragraphs.isEmpty()) {
                    getChapterFromNetwork(it.chapter)
                } else {
                    Single.just(it)
                }
            }
    }

    fun getChapterById(id: Int): Single<Chapter> {
        return chapterDao.getChapterById(id)
            .map { it.chapter }
    }

    private fun addAllBooks(bookList: List<Book>): Unit =
            bookDao.addAllBooks(*bookList.toTypedArray())

    private fun getChapterListFromNetwork(book: Book): Single<List<Chapter>> =
            network.getWuxiaChapterList(book)
                    .doOnSuccess { addChapters(it) }

    private fun addChapters(chapterList: List<Chapter>) {
        Log.d("Chapter", "adding chapters : $chapterList")
        chapterDao.addChapters(*chapterList.toTypedArray())
    }

//    private fun getChapterFromNetwork(id: Int): Single<ChapterParagraph> =
//            network.getWuxiaChapter(id)
//                    .map {
//                        it.forEachIndexed { index, paragraph ->
//                            chapterDao.addParagraph(
//                                Paragraph(id * 1000 + index, id, index, paragraph))
//                        }
//                        chapterDao.update(chapter)
//                    }
//                    .flatMap { chapterDao.getChapterById(chapter.id) }

    private fun getChapterFromNetwork(chapter: Chapter): Single<ChapterParagraph> =
            network.getWuxiaChapter(chapter)
                    .map {
                        it.forEachIndexed { index, paragraph ->
                            chapterDao.addParagraph(
                                Paragraph(chapter.id * 1000 + index, chapter.id, index, paragraph))
                        }
                        chapterDao.update(chapter)
                    }
                    .flatMap { chapterDao.getChapterById(chapter.id) }

    fun getChapterByNumber(number: Float, bookId: Int): Single<ChapterParagraph> {
        return chapterDao.getChapterByNumber(number, bookId)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    if (it.paragraphs.isEmpty()) {
                        getChapterFromNetwork(it.chapter)
                    } else {
                        Single.just(it)
                    }
                }
    }
}
