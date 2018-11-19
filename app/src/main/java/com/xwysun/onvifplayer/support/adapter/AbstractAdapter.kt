package com.xwysun.onvifplayer.support.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * Created by hupihuai on 2018/7/27.
 */
abstract class AbstractAdapter<ITEM>(protected var itemList: List<ITEM>, private val initHolder: (Holder,Int) -> Unit)
    : androidx.recyclerview.widget.RecyclerView.Adapter<AbstractAdapter.Holder>() {

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = createItemView(parent, viewType)
        val viewHolder = Holder(view)
        val itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        initHolder(viewHolder,viewType)
        return viewHolder
    }


    fun update(items: List<ITEM>) {
        updateAdapterWithDiffResult(calculateDiff(items))
    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    private fun calculateDiff(newItems: List<ITEM>) =
            DiffUtil.calculateDiff(DiffUtilCallback(itemList, newItems))

    fun add(item: ITEM) {
        itemList.toMutableList().add(item)
        notifyItemInserted(itemList.size)
    }

    fun remove(position: Int) {
        itemList.toMutableList().removeAt(position)
        notifyItemRemoved(position)
    }

    final override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        onViewRecycled(holder.itemView)
    }

    protected open fun onViewRecycled(itemView: View) {
    }

    protected open fun onItemClick(itemView: View, position: Int) {
    }

    protected abstract fun createItemView(parent: ViewGroup, viewType: Int): View

    class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val views = SparseArray<View>()

        fun <T : View> getView(viewId: Int): T {
            var view = views[viewId]
            if (view == null) {
                view = itemView.findViewById(viewId)
                views.put(viewId, view)
            }
            return view as T
        }
    }
}