package com.shelvz.assignment.kit.base

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.shelvz.assignment.kit.utils.DefaultDiffUtilsCallback
import java.util.*

/**
 * Created by shafic on 8/19/17.
 */

abstract class BaseAdapter<T: BaseModel, VH : RecyclerView.ViewHolder>(val context: Context, var data: MutableList<T> = 
        ArrayList()) : RecyclerView.Adapter<VH>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    init {
        //Do on Initialization
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        //Needed implementation
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.itemView != null) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(holder.adapterPosition, getDataItem(holder.adapterPosition))
                }
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(holder.adapterPosition, getDataItem(holder
                            .adapterPosition))
                    return@setOnLongClickListener true
                }
            }
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

    open fun update(newItems: List<T>) {
        val diffResult = DiffUtil.calculateDiff(DefaultDiffUtilsCallback(data, newItems))
        diffResult.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(newItems)
    }
    
    interface OnItemClickListener {
        fun <T> onItemClick(position: Int, item: T)
    }

    interface OnItemLongClickListener {
        fun <T> onItemLongClick(position: Int, item: T)
    }


}
