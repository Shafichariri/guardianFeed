package com.shelvz.assignment.adapters.DiffUtils

import com.shelvz.assignment.kit.utils.DefaultDiffUtilsCallback
import com.shelvz.assignment.models.Article


class DiffUtilsCallback(oldList: List<Article>, newList: List<Article>) :
        DefaultDiffUtilsCallback<Article>(oldList, newList)
