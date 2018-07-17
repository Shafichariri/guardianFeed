package com.shelvz.assignment.kit.base

import android.content.Context
import android.support.v7.widget.RecyclerView

abstract class BaseAdapterWithLoader<T : BaseModel, VH : RecyclerView.ViewHolder>
(context: Context, data: MutableList<T> = ArrayList()) : BaseAdapter<T, VH>(context, data) {

    var onLoaderItemClickListener: OnLoaderItemClickListener? = null
    private var showLoader: Boolean = false

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
            super.onBindViewHolder(holder, position)
        } else {
            //Do anything for the loader view
            if (onLoaderItemClickListener != null) {
                holder.itemView.setOnClickListener {
                    onLoaderItemClickListener?.onLoaderItemClick()
                }
            }
        }
    }

    interface OnLoaderItemClickListener {
        fun onLoaderItemClick()
    }
}
