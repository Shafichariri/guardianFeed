package com.shelvz.assignment.kit.databinding

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shelvz.assignment.kit.base.BaseAdapter


/**
 * Created by shafic on 8/19/17.
 */

abstract class DataBoundAbstractAdapter<T, VDB : ViewDataBinding>(context: Context, data: MutableList<T>) :
        BaseAdapter<T, DataBoundBaseViewHolder<T, VDB>>(context, data) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBoundBaseViewHolder<T, VDB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: VDB = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return DataBoundBaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundBaseViewHolder<T, VDB>,
                                  position: Int) {
        super.onBindViewHolder(holder, position)
        if (!isLoaderPosition(position = position)) {
            val item = getDataItem(position)
            onViewHolderBound(item, position, holder.binding, holder)
        }
        holder.executePending()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderPosition(position = position)) {
            getLoaderLayoutId()
        } else {
            getLayoutIdForPosition(position)
        }
    }

    open fun prepend(list: List<T>) {
        data.addAll(0, list)
        notifyItemRangeInserted(0, list.size)
    }

    open fun append(list: List<T>) {
        val sizeBefore = data.size
        data.addAll(sizeBefore, list)
        notifyItemRangeInserted(sizeBefore, data.size)
    }

    open fun set(list: List<T>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    open fun update(list: List<T>) {
        //Implement custom list update
    }

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun onViewHolderBound(item: T, position: Int, binding: VDB, holder: DataBoundBaseViewHolder<T, VDB>)
}
