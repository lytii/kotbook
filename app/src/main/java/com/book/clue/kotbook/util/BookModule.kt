package com.book.clue.kotbook.util

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BookModule {

    @Provides
    @Singleton
    fun provideNetwork(): Network = Network()
}
