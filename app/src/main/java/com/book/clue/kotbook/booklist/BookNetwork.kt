package com.book.clue.kotbook.booklist

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface BookNetwork {

    @GET
    fun getFromUrl(@Url url: String): Observable<ResponseBody>

}