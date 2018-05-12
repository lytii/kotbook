package com.book.clue.kotbook.db

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface ChapterDao {

    @Query("SELECT * FROM Chapter WHERE bookId = :bookId")
    fun getChapterList(bookId: Int): Single<List<Chapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChapters(vararg chapters: Chapter)

    @Query("SELECT * FROM Chapter WHERE id = :id")
    fun getChapterById(id: Int): Single<ChapterParagraph>

    @Query("SELECT * FROM Chapter WHERE suffixUrl = :url")
    fun getChapterByUrl(url: String): Single<ChapterParagraph>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addParagraph(paragraph: Paragraph)

    @Query("SELECT * FROM Chapter WHERE number = :number AND bookId = :bookId")
    fun getChapterByNumber(number: Float, bookId: Int): Single<ChapterParagraph>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(chapter: Chapter)
}