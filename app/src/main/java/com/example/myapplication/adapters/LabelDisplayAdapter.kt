package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class LabelDisplayAdapter<T>(private val context: Context, private val items: ArrayList<T>): RecyclerView.Adapter<LabelDisplayAdapter.LabelDisplayViewHolder>() {

    var onListInitialized: OnListInitialized<T>? = null
    var onItemClicked: OnItemClicked<T>? = null
    var lastItemClicked = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelDisplayViewHolder {
        return LabelDisplayViewHolder(LayoutInflater.from(context).inflate(R.layout.display_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LabelDisplayViewHolder, position: Int) {

        val item = items[position]

        holder.content.text = onListInitialized?.contentInitialize(item)

        val backgroundInitializedStatus = onListInitialized?.backgroundInitialize(item)

        if ( backgroundInitializedStatus != null ) {

            if (backgroundInitializedStatus) {
                holder.contentRoot.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.optionSelected
                    )
                )
            } else {
                holder.contentRoot.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.optionUnselected
                    )
                )
            }
        }

        holder.contentRoot.setOnClickListener {
            onItemClicked?.onItemClick(item, position)
            lastItemClicked = position
        }
    }

    fun updateItems(newItems: ArrayList<T>) {
        lastItemClicked = 0
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clear() {
        lastItemClicked = 0
        items.clear()
        notifyDataSetChanged()
    }

    interface OnItemClicked<T> {
        fun onItemClick(item: T, position: Int)
    }

    interface OnListInitialized<T> {
        fun contentInitialize(item: T): String
        fun backgroundInitialize(item: T): Boolean
    }

    class LabelDisplayViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var content: TextView = v.findViewById(R.id.content)
        var contentRoot: CardView = v.findViewById(R.id.contentRoot)

    }
}