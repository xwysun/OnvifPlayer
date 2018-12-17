package com.xwysun.onvifplayer.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwysun.onvifplayer.support.adapter.inflate

class BaseAdapter<T>(val layoutResourceId: Int, val items: List<T>, val init: (View, T) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.Adapter<BaseAdapter.ViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view =parent.inflate(layoutResourceId)
        return ViewHolder(view, init)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bindForecast(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder<in T>(view: View, val init: (View, T) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bindForecast(item: T) {
            with(item) {
                init(itemView, item)
            }
        }
    }
}
