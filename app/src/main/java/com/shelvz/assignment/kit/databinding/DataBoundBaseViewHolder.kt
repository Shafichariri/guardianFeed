package com.shelvz.assignment.kit.databinding

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by shafic on 8/19/17.
 */

open class DataBoundBaseViewHolder<in T, out VDB : ViewDataBinding>(val binding: VDB) : RecyclerView.ViewHolder(binding.root) {

    open fun bind(item: T) {
        // binding.setVariable(BR.item, item)
    }

    fun executePending() {
        //Execute Pending Bindings 
        binding.executePendingBindings()
    }
}
