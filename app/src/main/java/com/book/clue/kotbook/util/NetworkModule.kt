package com.book.clue.kotbook.util

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetwork(): Network = Network()
}
