package com.book.clue.kotbook.util

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WBookApi {

    @GET
    fun getFromUrl(@Url url: String): Single<ResponseBody>

    @GET("novels/")
    fun getBookList(): Single<ResponseBody>

    @GET("novels/chaptergroups/{bookId}")
    fun getGroupList(@Path("bookId") bookId: Int): Observable<ResponseBody>

    @GET("novels/chaptergroup/{groupId}")
    fun getGroupChapters(@Path("groupId") groupId: Int): Observable<ResponseBody>

    @GET("novels/chapters/{id}")
    fun getChapter(@Path("id") id: Int): Observable<ResponseBody>



    data class ChapterGroup(
            @SerializedName("ChapterGroupId") val groupId: Int,
            @SerializedName("FromChapterNumber") val starting: Int,
            @SerializedName("NovelId") val bookId: Int
    )

    //    val novelsApi = "http://gravitytales.com/api/novels"
//    val getChapterGroups = "http://gravitytales.com/api/novels/chaptergroups/<bookId>"
//    val getChapterListFromGroup = "http://gravitytales.com/api/novels/chaptergroup/<groupId>"
}