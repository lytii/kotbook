package com.book.clue.kotbook.util

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface BookNetwork {

    @GET
    fun getFromUrl(@Url url: String): Single<ResponseBody>

}