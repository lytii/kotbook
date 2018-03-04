package com.book.clue.kotbook.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface ChapterDao {

    @Query("SELECT * FROM Chapter WHERE bookId = :bookId")
    fun getChapterList(bookId: Int): Single<List<Chapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChapters(vararg chapters: Chapter)

    @Query("SELECT * FROM Chapter WHERE id = :id")
    fun getChapterById(id: Int): Single<ChapterParagraph>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addParagraph(paragraph: Paragraph)

}