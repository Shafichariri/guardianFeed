package com.shelvz.assignment.managers

import com.shelvz.assignment.models.Article

object LatestArticlesManager {
    //Temporary memory caching
    private var list: MutableList<Article> = mutableListOf()

    fun add(latest: List<Article>) {
        list.addAll(latest)
    }

    fun getAndClear(): List<Article> {
        val temp = list.toMutableList()
        clear()
        return temp
    }

    private fun clear() {
        list.clear()
    }
}
