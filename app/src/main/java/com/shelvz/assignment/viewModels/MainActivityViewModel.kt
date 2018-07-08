package com.shelvz.assignment.viewModels

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shelvz.assignment.kit.databinding.BaseViewModel
import com.shelvz.assignment.managers.ArticlesManager
import com.shelvz.assignment.models.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = MainActivityViewModel::class.java.simpleName
    
    private var liveList: MutableLiveData<MutableList<Article>> = MutableLiveData()
    private var isLoadingMore: MutableLiveData<Boolean> = MutableLiveData()

    init {
        liveList.value = mutableListOf()
        isLoadingMore.value = false
    }

    fun getList(): LiveData<MutableList<Article>> {
        return liveList
    }

    fun getIsLoadMore(): LiveData<Boolean> {
        return isLoadingMore
    }

    fun loadArticles() {
        ArticlesManager
                .getArticles(Schedulers.io(), AndroidSchedulers.mainThread(), pageNumber = 1)
                .subscribe({ articles: List<Article> ->
                    //Update
                    updateArticles(liveList, articles)
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
    }

    fun loadNextPage() {
        val isLoading = isLoadingMore.value ?: false
        if (!ArticlesManager.canLoadMore() || isLoading) return

        isLoadingMore.value = true

        ArticlesManager
                .getNextPageArticles(Schedulers.io(), AndroidSchedulers.mainThread())
                .subscribe({ articles: List<Article> ->
                    //Update
                    updateArticles(liveList, articles)

                    isLoadingMore.value = false
                }, { t: Throwable? ->
                    isLoadingMore.value = false
                    t?.printStackTrace()
                })
    }

    fun addArticleToMemory(article: Article) {
        ArticlesManager.addArticle(article, cache = false)
                .subscribe({ Log.e(TAG, "Success: $it | Removed") },
                        { it.printStackTrace() })
    }

    private fun updateArticles(liveList: MutableLiveData<MutableList<Article>>,
                               articles: List<Article>) {
        //Update liveList without notifying change
        liveList.value?.addAll(articles)
        //Post changes on liveList
        liveList.postValue(articles.toMutableList())
    }
}
