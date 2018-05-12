package com.book.clue.kotbook.booklist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.book.clue.kotbook.R
import com.book.clue.kotbook.db.Chapter
import kotlinx.android.synthetic.main.book_list_layout.view.*
import kotlin.reflect.KFunction1

class ChapterListAdapter(
        val items: List<Chapter>,
        val listener: KFunction1<@ParameterName(name = "chapter") Chapter, Unit>
) : RecyclerView.Adapter<ChapterListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(parent.inflate(R.layout.book_list_layout))
        holder.title.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener(items[pos])
            }
        }
        return holder
    }

    class ViewHolder(bookView: View) : RecyclerView.ViewHolder(bookView) {

        val title: TextView = bookView.title_text_view

        fun bind(chapter: Chapter) {
            title.text = chapter.title
        }
    }

    fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }
}
