package com.shelvz.assignment.kit.utils

import com.shelvz.assignment.kit.base.BaseModel

open class DefaultDiffUtilsCallback<T : BaseModel>(private val oldList: List<T>, private val newList: List<T>) :
        AbstractDiffUtilsCallback<T>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Data.getId() is String
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
