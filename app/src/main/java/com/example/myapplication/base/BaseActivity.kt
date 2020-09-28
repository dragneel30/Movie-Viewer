package com.example.myapplication.base

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.LabelDisplayAdapter


open abstract class BaseActivity : AppCompatActivity() {

    abstract fun initViews()
    abstract fun initObservers()
    abstract fun initDisplay()

    fun setLoading(v: View, loading: Boolean) {
        v.visibility = if ( loading ) View.VISIBLE else View.GONE
    }

    fun createSeatLabel(label: Char, baseLabel: TextView): TextView {
        val textView = TextView(this)
        textView.layoutParams = baseLabel.layoutParams
        textView.setTextColor(baseLabel.currentTextColor)
        textView.text = label.toString()
        textView.textSize = baseLabel.textSize
        return textView
    }


    fun <T>createRecycler(recylerView: RecyclerView, adapter: LabelDisplayAdapter<T>) {
        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.option_divider)
        if ( dividerDrawable != null ) {
            val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
            itemDecorator.setDrawable(dividerDrawable)
            recylerView.addItemDecoration(itemDecorator)
        }
        recylerView.adapter = adapter
        recylerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

}