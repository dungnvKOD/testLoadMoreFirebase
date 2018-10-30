package com.dung.demoloadmorefirebase

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.prpgress.view.*


internal class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtProgress = view.progressBar
}

internal class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtId: TextView = view.txtId
    val txtName: TextView = view.txtName
}

class MyAdapter(val context: Context, val recyclerView: RecyclerView, val items: ArrayList<User?>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG="MyAdapter"

    val VIEW_ITEM = 0
    val VIEW_LOADING = 1

    internal var loadMore: ILoadMore? = null
    internal var isLoading: Boolean = false
    internal var visibleThreshold: Int = 20 //so luong item toi da hien thi
    internal var lastVisibleItem: Int = 0 // vi tri item cuoi
    internal var totalItemCount: Int = 0 //tong so item

    private val inflater = LayoutInflater.from(context)

    init {
        val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                lastVisibleItem =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() //la ra vi tri cuoi cung cua item
                totalItemCount = linearLayoutManager.itemCount //lay tong so item

                /**
                 * Neu khong pahi trong thai loading
                 * va tong so item be hon hoac bang vi tri item cuoi
                 *  + so luong item toi da hien thi
                 */
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {

                    if (loadMore != null) {
                        //trang thai bat dau load la true
                        Log.d(TAG,"ok")
                        loadMore!!.loadMore(visibleThreshold, lastVisibleItem)  //interface nang nghe su kien loading
                        isLoading = true
                    }
                }

            }
        })


    }

    fun setLoaded() { //sau khi load xong data
        isLoading = false
    }

    fun setLoadMore(iLoadMore: ILoadMore) {
        this.loadMore = iLoadMore
    }

    override fun getItemViewType(position: Int): Int {

        return if (items[position] == null) {
            VIEW_LOADING

        } else {
            VIEW_ITEM
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return if (p1 == VIEW_ITEM) {
            val view = inflater.inflate(R.layout.item, p0, false)
            ItemViewHolder(view)

        } else {
            val view = inflater.inflate(R.layout.prpgress, p0, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ItemViewHolder) {
            val user: User? = items[p0.adapterPosition]
            p0.txtId.text = user!!.id
            p0.txtName.text = user.name

        } else if (p0 is LoadingViewHolder) {
            p0.txtProgress.isIndeterminate = true

        }
    }


}