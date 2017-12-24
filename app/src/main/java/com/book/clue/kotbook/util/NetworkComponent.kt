package com.book.clue.kotbook.util

import com.book.clue.kotbook.controllers.BookListController
import com.book.clue.kotbook.controllers.ChapterController
import com.book.clue.kotbook.controllers.ChapterListController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(BookModule::class))
interface NetworkComponent {

    fun inject(bookListController: BookListController)
    fun inject(chapterListController: ChapterListController)
    fun inject(chapterController: ChapterController)
}