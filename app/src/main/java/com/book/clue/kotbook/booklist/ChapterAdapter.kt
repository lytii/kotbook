package com.book.clue.kotbook.booklist

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.book.clue.kotbook.R
import kotlinx.android.synthetic.main.chapter_item_layout.view.*

class ChapterAdapter(
        val paragraphs: ArrayList<String>
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.chapter_item_layout))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(paragraphs.get(position))
    }

    override fun getItemCount() = paragraphs.size

    class ViewHolder(chapterView: View) : RecyclerView.ViewHolder(chapterView) {
        val text: TextView = chapterView.chapter_item_text_view

        fun bind(paragraph: String) {
            this.text.text = Html.fromHtml(paragraph)
        }
    }

    fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }
}