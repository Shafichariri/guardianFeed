package com.shelvz.assignment.activities

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shelvz.assignment.R
import com.shelvz.assignment.adapters.ArticlesAdapter
import com.shelvz.assignment.databinding.ActivityMainBinding
import com.shelvz.assignment.databinding.ItemArticleBinding
import com.shelvz.assignment.kit.base.BaseAdapter
import com.shelvz.assignment.kit.databinding.DataBoundActivity
import com.shelvz.assignment.models.Article
import com.shelvz.assignment.utilities.Action
import com.shelvz.assignment.utilities.LoadMoreListener
import com.shelvz.assignment.utilities.Mode
import com.shelvz.assignment.viewModels.MainActivityViewModel


class MainActivity : DataBoundActivity<ActivityMainBinding, MainActivityViewModel>(), BaseAdapter.OnItemClickListener {

    private val adapter: ArticlesAdapter by lazy { ArticlesAdapter(this, items = mutableListOf()) }
    private var loadMoreListener: LoadMoreListener? = null

    override fun classForViewModel(): Class<MainActivityViewModel> {
        return MainActivityViewModel::class.java
    }

    override fun layoutForDataBinding(): Int {
        return R.layout.activity_main
    }

    override fun onCreated(viewDataBinding: ActivityMainBinding, viewModel: MainActivityViewModel, savedInstanceState: Bundle?) {
        viewDataBinding.listener = this
        setupRecyclerView()

        val extras: Bundle? = intent.extras
        extras.apply {
            //Set the MainActivity loading Mode
            //Whether to load from the Local Db the cached/bookmarked articles
            //Or to load from the REMOTE Server (API)
            val modeId = this?.getInt(EXTRA_MODE_ID, Mode.REMOTE) ?: Mode.REMOTE
            viewModel.setModeWith(modeId)
            setupActionBar()
        }

        viewModel.getAction().observe(this, Observer { action ->
            adapter.update(viewModel.getList())
            if (action is Action.Prepend && action.count > 0) {
                viewDataBinding.pullUpButtonVisibility = true
            }
        })
        viewModel.getIsLoadMore().observe(this, Observer { isLoadingMore ->
            if (viewDataBinding.recyclerView.layoutManager == null) {
                viewDataBinding.recyclerView.postDelayed({ adapter.setShowLoader(isLoadingMore ?: false) }, 100)
            } else {
                adapter.setShowLoader(isLoadingMore ?: false)
            }
        })
        viewModel.loadArticles()

        adapter.onItemClickListener = this
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isCachedMode()) {
            //Reload in-case cached items changed
            viewModel.reloadCachedArticles()
        }
    }

    //region D A T A B O U N D  C L I C K  L I S T E N E R S    
    fun onPullUpButtonClick(view: View) {
        viewDataBinding.pullUpButtonVisibility = false
        viewDataBinding.recyclerView.smoothScrollToPosition(0)
    }
    //endregion

    //region T O O L B A R  A N D  M E N U
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.action_cached)?.isVisible = !viewModel.isCachedMode()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.action_cached -> {
                val cacheListIntent = MainActivity.getIntent(this, localData = true)
                startActivity(cacheListIntent)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
                false //We Do not consume so that it would be passed to the children fragment
            }
        }
    }
    //endregion

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setupLoadMoreListener()
    }

    private fun setupActionBar() {
        setSupportActionBar(viewDataBinding.toolbar)
        if (viewModel.isCachedMode()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = getString(R.string.cached)
        }
        invalidateOptionsMenu()
    }

    private fun setupRecyclerView() {
        viewDataBinding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewDataBinding.recyclerView.adapter = adapter
        setupLoadMoreListener()
    }

    private fun setupLoadMoreListener() {
        if (loadMoreListener != null) {
            viewDataBinding.recyclerView.removeOnScrollListener(loadMoreListener)
        }
        loadMoreListener = createLoadMoreListener()
        viewDataBinding.recyclerView.addOnScrollListener(loadMoreListener)
    }

    private fun createLoadMoreListener(): LoadMoreListener = object : LoadMoreListener() {
        override fun onLoadMore() {
            viewModel.loadNextPage()
        }
    }

    override fun <T> onItemClick(position: Int, item: T) {
        if (item is Article) {
            viewModel.addArticleToMemory(item)

            val vh = viewDataBinding.recyclerView.findViewHolderForAdapterPosition(position)
            val binding = DataBindingUtil.getBinding<ItemArticleBinding>(vh.itemView) ?: return
            val sharedImageView = binding.imageViewThumbnail
            val titleTextView = binding.textViewTitle
            val thumbnailUrl = item.fields?.thumbnail ?: ""
            val intent = ArticleDetailsActivity.getIntent(this)

            intent.putExtra(EXTRA_ARTICLE_ID, item.id)
            intent.putExtra(EXTRA_ARTICLE_IMAGE_URL, thumbnailUrl)
            intent.putExtra(EXTRA_IMAGE_TRANSITION, ViewCompat.getTransitionName(sharedImageView))
            if (thumbnailUrl.isNotBlank()) {
                val imageTransitionPair: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair(sharedImageView,
                        ViewCompat.getTransitionName(sharedImageView))
                val textTransitionPair: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair(titleTextView,
                        ViewCompat.getTransitionName(titleTextView))
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        imageTransitionPair, textTransitionPair)
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }


        }
    }

    companion object {
        private const val EXTRA_MODE_ID = "modeId"
        const val EXTRA_ARTICLE_ID = "articleId"
        const val EXTRA_ARTICLE_IMAGE_URL = "articleImageUrl"
        const val EXTRA_IMAGE_TRANSITION = "imageTransition"

        fun getIntent(context: Context, localData: Boolean = false): Intent =
                Intent(context, MainActivity::class.java).apply {
                    val modeId: Int = if (localData) Mode.LOCAL else Mode.REMOTE
                    putExtra(EXTRA_MODE_ID, modeId)
                }
    }
}
