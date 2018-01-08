package com.book.clue.kotbook.dagger

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    fun get() = context
}
