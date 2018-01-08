package com.book.clue.kotbook.dagger

import android.arch.persistence.room.Room
import android.content.Context
import com.book.clue.kotbook.db.BookDao
import com.book.clue.kotbook.db.BookDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ContextModule::class))
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): BookDatabase =
            Room.databaseBuilder(context, BookDatabase::class.java, "bookdb").build()

    @Provides
    @Singleton
    fun bookDao(bookDatabase: BookDatabase): BookDao =
            bookDatabase.bookDao()
}
