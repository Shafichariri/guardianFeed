package com.shelvz.assignment.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import com.shelvz.assignment.R
import com.shelvz.assignment.adapters.DiffUtils.DiffUtilsCallback
import com.shelvz.assignment.databinding.ItemArticleBinding
import com.shelvz.assignment.kit.databinding.DataBoundAbstractAdapter
import com.shelvz.assignment.kit.databinding.DataBoundBaseViewHolder
import com.shelvz.assignment.models.Article


class ArticlesAdapter(context: Context, items: MutableList<Article>) :
        DataBoundAbstractAdapter<Article, ItemArticleBinding>(context, items) {

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_article
    }

    override fun onViewHolderBound(item: Article, position: Int, binding: ItemArticleBinding, holder: DataBoundBaseViewHolder<Article, ItemArticleBinding>) {
        binding.category = item.type.toUpperCase()
        binding.imageUrl = item.fields?.thumbnail
        binding.title = item.webTitle
    }

    override fun getLoaderLayoutId(): Int {
        return R.layout.item_load_more
    }

    override fun update(newItems: List<Article>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilsCallback(data, newItems))
        diffResult.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(newItems)
    }
}
