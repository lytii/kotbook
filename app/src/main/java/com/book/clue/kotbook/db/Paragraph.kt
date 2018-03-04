package com.book.clue.kotbook.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Paragraph(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val chapterId: Int,
    val number: Int,
    val paragraph: String
): Serializable