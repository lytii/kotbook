package com.book.clue.kotbook.util

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WuxiaApi {
    // https://www.wuxiaworld.com/novel/overgeared/og-chapter-201
    @GET("novel/{book}/{chapter}")
    fun getChapter(@Path("book") book: String,
                   @Path("chapter") chapter: String): Single<ResponseBody>

    @GET("tag/{tag}")
    fun getList(@Path("tag") tag: String): Single<ResponseBody>

    @GET()
    fun getUrl(@Url path:String): Single<ResponseBody>
}