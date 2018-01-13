package com.book.clue.kotbook.dagger

import com.book.clue.kotbook.MainActivity
import com.book.clue.kotbook.controllers.BookListController
import com.book.clue.kotbook.controllers.ChapterController
import com.book.clue.kotbook.controllers.ChapterListController
import com.book.clue.kotbook.util.Network
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseModule::class))
abstract class NetworkComponent {

    companion object {
        fun get(): NetworkComponent = SingletonHolder.INSTANCE
    }

    private class SingletonHolder {
        companion object {
            val INSTANCE = DaggerNetworkComponent
                    .builder()
                    .contextModule(ContextModule(MainActivity.context))
                    .build()!!
        }
    }

    abstract fun network(): Network

    abstract fun inject(bookListController: BookListController)
    abstract fun inject(chapterListController: ChapterListController)
    abstract fun inject(chapterController: ChapterController)
    abstract fun inject(mainActivity: MainActivity)
}
