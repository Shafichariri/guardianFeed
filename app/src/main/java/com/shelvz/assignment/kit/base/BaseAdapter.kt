package com.shelvz.assignment.kit.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import java.util.*

/**
 * Created by shafic on 8/19/17.
 */

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(val context: Context, var data: MutableList<T> = ArrayList()) : RecyclerView.Adapter<VH>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private var showLoader: Boolean = false

    init {
        //Do on Initialization
    }

    abstract fun getLoaderLayoutId(): Int

    private fun shouldShowLoader(): Boolean {
        return showLoader
    }

    fun setShowLoader(visible: Boolean) {
        when {
            (showLoader == visible) -> {
                //Do nothing
            }
            visible -> {
                showLoader = true
                notifyItemInserted(itemCount - 1)
            }
            else -> {
                showLoader = false
                notifyItemRemoved(itemCount)
            }
        }
    }

    fun isLoaderPosition(position: Int): Boolean {
        return shouldShowLoader() && position == itemCount - 1
    }

    override fun getItemCount(): Int {
        var itemCount = data.size

        if (shouldShowLoader()) {
            itemCount++
        }

        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderPosition(position = position)) {
            getLoaderLayoutId()
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!isLoaderPosition(position = position)) {
            if (holder.itemView != null) {
                if (onItemClickListener != null) {
                    holder.itemView.setOnClickListener {
                        onItemClickListener!!.onItemClick(holder.adapterPosition, getDataItem(holder.adapterPosition))
                    }
                }

                if (onItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener {
                        onItemLongClickListener!!.onItemLongClick(holder.adapterPosition, getDataItem(holder.adapterPosition))
                        return@setOnLongClickListener true
                    }
                }
            }
        } else {
            //Do anything for the loader view
        }
    }

    /**
     * Get items safely with regards to the extra custom views
     * if any above the data or below the data
     * (but not the load more | its below).
     * */
    fun getDataItem(position: Int): T {
        val dataPosition = getDataRelativePosition(position)
        return data[dataPosition]
    }

    open fun getDataRelativePosition(position: Int): Int {
        var returnPositionValue = position

        //if (isShowingAnyCustomViewBeforeThelastItem()) {
        //    returnPositionValue--
        //}

        return returnPositionValue
    }

    interface OnItemClickListener {
        fun <T> onItemClick(position: Int, item: T)
    }

    interface OnItemLongClickListener {
        fun <T> onItemLongClick(position: Int, item: T)
    }


}
