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

class ArticleDetailsActivityViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = ArticleDetailsActivityViewModel::class.java.simpleName
    private var articleLiveData: MutableLiveData<Article> = MutableLiveData()

    fun getArticleLiveData(): LiveData<Article> {
        return articleLiveData
    }

    fun loadArticle(articleId: String) {
        val flowable = ArticlesManager.getArticle(articleId)

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ article ->
                    val loadedArticle = article.value ?: return@subscribe
                    if (loadedArticle != null) {
                        this.articleLiveData.value = loadedArticle
                    } else {
                        //TODO: handle error, something went wrong!
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                    //TODO: handle error, something went wrong!
                })
    }

    fun bookmarkArticle(completionBlock: ((success: Boolean, bookmark: Boolean) -> Unit)) {
        var success = false
        var bookmarked = false
        //Get article or fail
        val article = articleLiveData.value ?: return completionBlock(success, bookmarked)
        val id = articleLiveData.value?.id ?: return completionBlock(success, bookmarked)

        val foundFlowable = ArticlesManager.getArticle(id, cache = true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.value != null }


        foundFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { found ->
                    //If it was found then it was already bookmarked so the new bookmarked value 
                    //is the inverse
                    bookmarked = !found
                    return@flatMap if (!found) {
                        //If it was not found we should add it
                        ArticlesManager
                                .addArticle(subscribeOn = Schedulers.io(),
                                        observeOn = AndroidSchedulers.mainThread(),
                                        article = article, cache = true)
                    } else {
                        //If it was found so we should remove it
                        ArticlesManager
                                .removeArticle(subscribeOn = Schedulers.io(),
                                        observeOn = AndroidSchedulers.mainThread(),
                                        article = article, cache = true)
                    }
                }
                .subscribe({ _: Boolean ->
                    success = true
                    completionBlock(success, bookmarked)
                }, { throwable: Throwable ->
                    throwable.printStackTrace()
                    success = false
                    completionBlock(success, bookmarked)
                })
    }

    fun isArticleBookmarked(completionBlock: (success: Boolean, bookmarked: Boolean) -> Unit) {
        var success = false
        var bookmarked = false

        //Get event id or fail
        val id = articleLiveData.value?.id ?: return completionBlock(success, bookmarked)

        val foundFlowable = ArticlesManager.getArticle(id, cache = true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.value != null }

        foundFlowable
                .subscribe({ found ->
                    bookmarked = found
                    success = true
                    completionBlock(success, bookmarked)
                }, { throwable: Throwable ->
                    bookmarked = false
                    success = false
                    throwable.printStackTrace()
                    completionBlock(success, bookmarked)
                })
    }

    fun removeArticle() {
        val article = articleLiveData.value ?: return
        //Remove article from memory only
        ArticlesManager
                .removeArticle(article = article, cache = false)
                .subscribe({ Log.e(TAG, "Success: $it | Removed") },
                        { it.printStackTrace() })
    }
}
