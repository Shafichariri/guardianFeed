package com.shelvz.assignment.viewModels

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.shelvz.assignment.kit.databinding.BaseViewModel
import com.shelvz.assignment.managers.ArticlesManager
import com.shelvz.assignment.models.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleDetailsActivityViewModel(application: Application) : BaseViewModel(application) {
    private var articleLiveData: MutableLiveData<Article> = MutableLiveData()

    fun getArtileLiveData(): LiveData<Article> {
        return articleLiveData
    }

    fun loadArticle(articleId: String) {
        val flowable = ArticlesManager.getArticle(articleId)

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ article ->
                    this.articleLiveData.value = article
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
    }

    fun cacheArticle() {
        val article = articleLiveData.value ?: return
        ArticlesManager.addArticle(article, cache = true)
    }

    fun removeArticle() {
        val article = articleLiveData.value ?: return
        ArticlesManager.removeArticle(article, cache = false)
    }
}
