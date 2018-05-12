package com.book.clue.kotbook.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Book(
        @SerializedName("Id") @PrimaryKey() val id: Int,
        @SerializedName("Name") val name: String,
        @SerializedName("CoverUrl") val coverUrl: String,
        @SerializedName("Slug") var url: String,
        var synposis: String,
        var description: String
) : Serializable, Comparable<Book> {
    override fun compareTo(other: Book): Int {
        return name.compareTo(other.name)
    }
}