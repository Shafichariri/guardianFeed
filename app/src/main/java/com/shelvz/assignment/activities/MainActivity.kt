package com.shelvz.assignment.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.shelvz.assignment.R
import com.shelvz.assignment.adapters.ArticlesAdapter
import com.shelvz.assignment.databinding.ActivityMainBinding
import com.shelvz.assignment.kit.databinding.DataBoundActivity
import com.shelvz.assignment.utilities.LoadMoreListener
import com.shelvz.assignment.viewModels.MainActivityViewModel

class MainActivity : DataBoundActivity<ActivityMainBinding, MainActivityViewModel>() {

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
}
