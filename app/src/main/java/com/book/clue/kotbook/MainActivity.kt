package com.book.clue.kotbook

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.book.clue.kotbook.controllers.BookListController
import com.book.clue.kotbook.util.Network
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : Activity() {

    companion object {
        lateinit var instance: MainActivity
    }

    @Inject
    lateinit var network: Network
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this

        val container = controller_container as ViewGroup
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(BookListController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
