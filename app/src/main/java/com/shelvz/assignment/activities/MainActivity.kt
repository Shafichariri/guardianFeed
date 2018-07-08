package com.shelvz.assignment.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.shelvz.assignment.R
import com.shelvz.assignment.adapters.ArticlesAdapter
import com.shelvz.assignment.databinding.ActivityMainBinding
import com.shelvz.assignment.kit.base.BaseAdapter
import com.shelvz.assignment.kit.databinding.DataBoundActivity
import com.shelvz.assignment.models.Article
import com.shelvz.assignment.utilities.LoadMoreListener
import com.shelvz.assignment.viewModels.MainActivityViewModel


class MainActivity : DataBoundActivity<ActivityMainBinding, MainActivityViewModel>(), BaseAdapter.OnItemClickListener {

    private val adapter: ArticlesAdapter by lazy { ArticlesAdapter(this, items = mutableListOf()) }

    override fun classForViewModel(): Class<MainActivityViewModel> {
        return MainActivityViewModel::class.java
    }

    override fun layoutForDataBinding(): Int {
        return R.layout.activity_main
    }

    override fun onCreated(viewDataBinding: ActivityMainBinding, viewModel: MainActivityViewModel, savedInstanceState: Bundle?) {
        setupRecyclerView()

        viewModel.getList().observe(this, Observer { list ->
            adapter.append(list ?: mutableListOf())
        })
        viewModel.getIsLoadMore().observe(this, Observer { isLoadingMore ->
            adapter.setShowLoader(isLoadingMore ?: false)
        })
        viewModel.loadArticles()

        adapter.onItemClickListener = this
    }

    private fun setupRecyclerView() {
        viewDataBinding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewDataBinding.recyclerView.adapter = adapter
        viewDataBinding.recyclerView.addOnScrollListener(object : LoadMoreListener() {
            override fun onLoadMore() {
                viewModel.loadNextPage()
            }
        })
    }

    override fun <T> onItemClick(position: Int, item: T) {
        if (item is Article) {
            viewModel.addArticleToMemory(item)

            val vh = viewDataBinding.recyclerView.findViewHolderForAdapterPosition(position)
            val sharedImageView = vh.itemView.findViewById<ImageView>(R.id.imageView_thumbnail)
            val titleTextView = vh.itemView.findViewById<AppCompatTextView>(R.id.textView_title)
            val intent = Intent(this, ArticleDetailsActivity::class.java)

            intent.putExtra(EXTRA_ARTICLE_ID, item.id)
            intent.putExtra(EXTRA_ARTICLE_IMAGE_URL, item.fields?.thumbnail ?: "")
            intent.putExtra(EXTRA_IMAGE_TRANSITION, ViewCompat.getTransitionName(sharedImageView))

            val imageTransitionPair: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair(sharedImageView,
                    ViewCompat.getTransitionName(sharedImageView))
            val textTransitionPair: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair(titleTextView,
                    ViewCompat.getTransitionName(titleTextView))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    imageTransitionPair, textTransitionPair)

            startActivity(intent, options.toBundle())
        }
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "articleId"
        const val EXTRA_ARTICLE_IMAGE_URL = "articleImageUrl"
        const val EXTRA_IMAGE_TRANSITION = "imageTransition"
    }
}
