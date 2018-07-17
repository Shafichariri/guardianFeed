package com.shelvz.assignment.adapters

import android.content.Context
import com.shelvz.assignment.R
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
}
