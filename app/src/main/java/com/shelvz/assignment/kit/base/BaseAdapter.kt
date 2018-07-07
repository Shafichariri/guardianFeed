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

    init {
        //Do on Initialization
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.itemView != null) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener { onItemClickListener!!.onItemClick(position, data[position]) }
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener {
                    onItemLongClickListener!!.onItemLongClick(position, data[position])
                    return@setOnLongClickListener true
                }
            }
        }
    }

    interface OnItemClickListener {
        fun <T> onItemClick(position: Int, item: T)
    }

    interface OnItemLongClickListener {
        fun <T> onItemLongClick(position: Int, item: T)
    }


}
