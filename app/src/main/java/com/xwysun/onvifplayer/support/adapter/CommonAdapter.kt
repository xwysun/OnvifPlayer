package com.xwysun.onvifplayer.support.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by hupihuai on 2018/7/27.
 */
class SingleAdapter<ITEM>(items: List<ITEM>,
                          private val layoutResId: Int,
                          initHolder: (Holder, Int) -> Unit)
    : AbstractAdapter<ITEM>(items, initHolder) {

    private var itemClick: (ITEM) -> Unit = {}
    private lateinit var bindHolder: (Holder, ITEM) -> Unit

    constructor(items: List<ITEM>,
                layoutResId: Int,
                initHolder: (AbstractAdapter.Holder, Int) -> Unit,
                bindHolder: (Holder, ITEM) -> Unit,
                itemClick: (ITEM) -> Unit = {}) : this(items, layoutResId, initHolder) {
        this.itemClick = itemClick
        this.bindHolder = bindHolder
    }

    override fun createItemView(parent: ViewGroup, viewType: Int): View {
        var view = parent inflate layoutResId
        if (view.tag?.toString()?.contains("layout/") == true) {
            DataBindingUtil.bind<ViewDataBinding>(view)
        }
        return view
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bindHolder(holder, itemList[position])
    }

    override fun onItemClick(itemView: View, position: Int) {
        itemClick(itemList[position])
    }
}


class MultiAdapter<ITEM : ListItemI>(private val items: List<ITEM>,
                                     initHolder: (Holder, Int) -> Unit)
    : AbstractAdapter<ITEM>(items, initHolder) {

    private var itemClick: (ITEM) -> Unit = {}
    private lateinit var bindHolder: (Holder, ITEM) -> Unit
    private lateinit var listItems: Array<out ListItem<ITEM>>

    constructor(items: List<ITEM>,
                listItems: Array<out ListItem<ITEM>>,
                initHolder: (Holder, Int) -> Unit,
                bindHolder: (Holder, ITEM) -> Unit,
                itemClick: (ITEM) -> Unit = {}) : this(items, initHolder) {
        this.itemClick = itemClick
        this.listItems = listItems
        this.bindHolder = bindHolder
    }


    override fun createItemView(parent: ViewGroup, viewType: Int): View {
        var view = parent inflate getLayoutId(viewType)
        if (view.tag?.toString()?.contains("layout/") == true) {
            DataBindingUtil.bind<ViewDataBinding>(view)
        }
        return view
    }

    private fun getLayoutId(viewType: Int): Int {
        var layoutId = -1
        listItems.forEach {
            if (it.layoutResId == viewType) {
                layoutId = it.layoutResId
                return@forEach
            }
        }
        return layoutId
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bindHolder(holder, itemList[position])
    }

    override fun onItemClick(itemView: View, position: Int) {
        itemClick(itemList[position])
    }
}


fun <ITEM> androidx.recyclerview.widget.RecyclerView.setUp(items: List<ITEM>,
                                                           layoutResId: Int,
                                                           initHolder: (AbstractAdapter.Holder) -> Unit = {},
                                                           bindHolder: (AbstractAdapter.Holder, ITEM) -> Unit,
                                                           itemClick: (ITEM) -> Unit = {},
                                                           manager: androidx.recyclerview.widget.RecyclerView.LayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
): AbstractAdapter<ITEM> {
    val singleAdapter by lazy {
        SingleAdapter(items, layoutResId, { holder, _ ->
            initHolder(holder)
        }, { holder, item ->
            bindHolder(holder, item)
        }, {
            itemClick(it)
        })
    }
    layoutManager = manager
    adapter = singleAdapter
    return singleAdapter
}


fun <ITEM : ListItemI> androidx.recyclerview.widget.RecyclerView.setUP(items: List<ITEM>,
                                                                       vararg listItems: ListItem<ITEM>,
                                                                       manager: androidx.recyclerview.widget.RecyclerView.LayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
): AbstractAdapter<ITEM> {

    val multiAdapter by lazy {
        MultiAdapter(items, listItems, { holder, viewType ->
            var listItem: ListItem<ITEM>? = getListItem(listItems, viewType)
            listItem?.initHolder?.invoke(holder)
        }, { holder, item ->
            var listItem: ListItem<ITEM>? = getListItem(listItems, item.getType())
            listItem?.bindHolder?.invoke(holder, item)
        }, { item ->
            var listItem: ListItem<ITEM>? = getListItem(listItems, item.getType())
            listItem?.itemClick?.invoke(item)
        })
    }
    layoutManager = manager
    adapter = multiAdapter
    return multiAdapter
}

private fun <ITEM : ListItemI> getListItem(listItems: Array<out ListItem<ITEM>>, type: Int): ListItem<ITEM>? {
    var listItem: ListItem<ITEM>? = null
    listItems.forEach {
        if (it.layoutResId == type) {
            listItem = it
            return@forEach
        }
    }
    return listItem
}

class ListItem<ITEM>(val layoutResId: Int,
                     val bindHolder: (holder: AbstractAdapter.Holder, item: ITEM) -> Unit,
                     val itemClick: (item: ITEM) -> Unit = {},
                     val initHolder: (AbstractAdapter.Holder) -> Unit = {}
)


interface ListItemI {
    fun getType(): Int
}

class ListItemAdapter<ITEM>(var data: ITEM, private val viewType: Int) : ListItemI {

    override fun getType(): Int {
        return viewType
    }
}