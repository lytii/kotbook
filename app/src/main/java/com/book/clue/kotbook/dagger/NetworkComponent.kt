package com.book.clue.kotbook.dagger

import android.content.Context
import com.book.clue.kotbook.MainActivity
import com.book.clue.kotbook.controllers.BookListController
import com.book.clue.kotbook.controllers.ChapterController
import com.book.clue.kotbook.controllers.ChapterListController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(BookModule::class, ContextModule::class))
interface NetworkComponent {

    fun context(): Context

    fun inject(bookListController: BookListController)
    fun inject(chapterListController: ChapterListController)
    fun inject(chapterController: ChapterController)
    fun inject(mainActivity: MainActivity)
}