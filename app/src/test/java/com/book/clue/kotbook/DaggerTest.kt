package com.book.clue.kotbook

import com.book.clue.kotbook.dagger.NetworkComponent
import com.book.clue.kotbook.util.Network
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject

class DaggerTest {

    @Inject
    lateinit var retrofit: Retrofit

    @Test
    fun getDagger() {
        val k = NetworkComponent.get().network()
        assert(k is Network)
    }
}