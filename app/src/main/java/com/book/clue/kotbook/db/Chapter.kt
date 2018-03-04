package com.book.clue.kotbook.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Chapter(
        @SerializedName("ChapterId") @PrimaryKey val id: Int,
        @SerializedName("Number") val number: Int,
        @SerializedName("Name") val title: String,
        @SerializedName("Slug") val suffixUrl: String,
        var bookId: Int
): Serializable