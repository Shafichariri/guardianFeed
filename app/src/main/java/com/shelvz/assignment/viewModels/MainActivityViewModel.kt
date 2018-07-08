package com.shelvz.assignment.viewModels

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shelvz.assignment.kit.databinding.BaseViewModel
import com.shelvz.assignment.managers.ArticlesManager
import com.shelvz.assignment.managers.BookmarksManager
import com.shelvz.assignment.models.Article
import com.shelvz.assignment.network.NetworkConstants
import com.shelvz.assignment.utilities.Action
import com.shelvz.assignment.utilities.Mode
import com.shelvz.assignment.utilities.toDate
import com.shelvz.assignment.utilities.toString
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivityViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = MainActivityViewModel::class.java.simpleName
    //Pair of Boolean and a List: Boolean is used to know if adapter should be reset or appended  
    private var liveAction: MutableLiveData<Action> = MutableLiveData()
    private var list: MutableList<Article> = mutableListOf()
    private var isLoadingMore: MutableLiveData<Boolean> = MutableLiveData()
    private var mode: Mode? = Mode.None()
    private var repeatDisposable: Disposable? = null
    private var apiDisposable: Disposable? = null

    init {
        isLoadingMore.value = false
    }

    fun setModeWith(id: Int) {
        mode = Mode.with(id)
    }

    fun isCachedMode(): Boolean = mode is Mode.Local

    fun getAction(): LiveData<Action> {
        return liveAction
    }

    fun getList(): MutableList<Article> {
        return list
    }

    fun getIsLoadMore(): LiveData<Boolean> {
        return isLoadingMore
    }

    fun getLatestArticleDate(): String? {
        val latestDate = list.filter { it.webPublicationDate != null }
                .mapNotNull { it.webPublicationDate?.toDate() }
                .sorted().lastOrNull()
        val strDate = latestDate?.toString(format = NetworkConstants.API_STRING_DATE_FULL_FORMAT_2)
        return strDate
    }

    override fun onCleared() {
        disposeFrom(repeatDisposable)
        disposeFrom(apiDisposable)
        super.onCleared()
    }

    fun disposeFrom(disposable: Disposable?) {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    private fun startPullIfNeeded() {
        if (!isCachedMode()) {
            startThirtySecondsPull()
        }
    }

    private fun startThirtySecondsPull() {
        disposeFrom(repeatDisposable)

        repeatDisposable = Flowable
                .fromCallable { getLatestFlowable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(30, TimeUnit.SECONDS)
                .repeat()
                .subscribe({
                    disposeFrom(apiDisposable)
                    apiDisposable = it
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                var addedItemsCount = 0
                                it.forEach { item ->
                                    val found = list.find {
                                        return@find item.id == it.id
                                    } != null
                                    if (!found) {
                                        //Add to list only if it is a new item
                                        list.add(0, item)
                                        addedItemsCount++
                                    }
                                }

                                liveAction.postValue(Action.Prepend(addedItemsCount))
                            }, { it.printStackTrace() })
                }, { it.printStackTrace() })
    }

    private fun getLatestFlowable(): Flowable<List<Article>> {
        val latestDate = getLatestArticleDate()
        return if (latestDate != null) {
            ArticlesManager
                    .getLatestArticles(pageSize = 100, dateFrom = latestDate)
        } else {
            ArticlesManager
                    .getArticles(Schedulers.io(), AndroidSchedulers.mainThread(), pageNumber = 1)
        }
    }

    fun reloadCachedArticles() {
        BookmarksManager.get()
                .subscribe({ articles: List<Article> ->
                    val list = list
                    var changed = false
                    list.forEach { item ->
                        changed = articles.find { it.id == item.id } != null
                        if (changed) {
                            return@forEach
                        }
                    }
                    //Update
                    list.clear()
                    list.addAll(articles)
                    liveAction.postValue(Action.Reload())
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
    }

    fun loadArticles() {
        val mode = mode ?: return
        if (mode is Mode.None) return
        //Config change will trigger loadArticles so we exit if the list already exists
        if (list.isNotEmpty()) return

        isLoadingMore.value = true

        //Change data source according to mode
        val flowable: Flowable<List<Article>> = when (mode) {
            is Mode.Local -> BookmarksManager.get()
            else -> ArticlesManager.getArticles(Schedulers.io(), AndroidSchedulers.mainThread(), pageNumber = 1)
        }

        flowable
                .subscribe({ articles: List<Article> ->
                    isLoadingMore.value = false
                    //Update
                    list.clear()
                    list.addAll(articles)
                    liveAction.postValue(Action.Reload())
                    startPullIfNeeded()
                }, { t: Throwable? ->
                    t?.printStackTrace()
                    isLoadingMore.value = false
                    startPullIfNeeded()
                })
    }

    fun loadNextPage() {
        val mode = mode ?: return
        //In local Mode we do not have paging
        if (mode is Mode.Local || mode is Mode.None) return

        val isLoading = isLoadingMore.value ?: false
        if (!ArticlesManager.canLoadMore() || isLoading) return

        isLoadingMore.value = true

        ArticlesManager
                .getNextPageArticles(Schedulers.io(), AndroidSchedulers.mainThread())
                .subscribe({ articles: List<Article> ->
                    //Update
                    list.addAll(articles)
                    liveAction.postValue(Action.Add())

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
}

