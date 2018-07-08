package com.shelvz.assignment.adapters.DiffUtils

import android.support.v7.util.DiffUtil
import com.shelvz.assignment.models.Article


class DiffUtilsCallback(private val oldList: List<Article>, private val newList: List<Article>) :
        DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Data.getId() is String
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
