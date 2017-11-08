package com.book.clue.kotbook.booklist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import com.book.clue.kotbook.R
import kotlinx.android.synthetic.main.book_list_layout.view.*
import kotlin.reflect.KFunction2

class BookListAdapter(
        val items: List<BookListItem>,
        val listener: KFunction2<@ParameterName(name = "title") String, @ParameterName(name = "url") String, Unit>
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
                listener(items[pos].bookTitle, items[pos].url)
            }
        }
        return holder
    }

    class ViewHolder(bookView: View) : RecyclerView.ViewHolder(bookView) {

        val title: TextView = bookView.title_text_view
        val favorite: ToggleButton = bookView.favorite_toggle_button

        fun bind(book: BookListItem) {
            title.text = book.bookTitle
            favorite.isChecked = book.isFavorite
        }
    }

    fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }
}
