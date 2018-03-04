package com.book.clue.kotbook.util

import com.google.gson.annotations.SerializedName

data class ChapterContent(
        @SerializedName("Content") val content: String
)