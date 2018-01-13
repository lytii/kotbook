package com.book.clue.kotbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.book.clue.kotbook.controllers.BookListController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    companion object {
        lateinit var context: MainActivity
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        val container = controller_container as ViewGroup
        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(BookListController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.getStringExtra(getString(R.string.bookUrl))
        super.onActivityResult(requestCode, resultCode, data)
    }
}
