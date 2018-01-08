package com.book.clue.kotbook.dagger

import android.arch.persistence.room.Room
import android.content.Context
import com.book.clue.kotbook.db.BookDatabase
import com.book.clue.kotbook.util.Network
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class BookModule {

    @Provides
    @Singleton
    fun provideNetwork(): Network = Network()

    @Provides
    @Singleton
    @Inject
    fun provideDatabase(context: Context) =
            Room.databaseBuilder(context, BookDatabase::class.java, "bookdb").build()

    @Provides
    @Singleton
    @Inject
    fun provideBookDao(bookDatabase: BookDatabase) = bookDatabase.bookDao()


}
