package com.book.clue.kotbook.db

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class ChapterParagraph {

    @Embedded
    lateinit var chapter: Chapter

    @Relation(parentColumn = "id", entityColumn = "chapterId",
            entity = Paragraph::class, projection = arrayOf("paragraph"))
    lateinit var paragraphs: List<String>

}