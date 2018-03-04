package com.book.clue.kotbook.booklist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.book.clue.kotbook.R
import com.book.clue.kotbook.db.Book
import kotlinx.android.synthetic.main.book_list_layout.view.*
import kotlin.reflect.KFunction1

class BookListAdapter(
        val items: List<Book>,
        val listener: KFunction1<@ParameterName(name = "book") Book, Unit>
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

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

        fun bind(book: Book) {
            title.text = book.name
//            favorite.isChecked = book.isFavorite
        }
    }

    fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }
}
