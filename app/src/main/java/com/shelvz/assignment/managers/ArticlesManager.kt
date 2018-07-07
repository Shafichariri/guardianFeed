package com.shelvz.assignment.managers

import com.shelvz.assignment.models.Article
import com.shelvz.assignment.models.PageInfo
import com.shelvz.assignment.network.NetworkManager
import com.shelvz.assignment.network.apiServices.SearchServices
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ArticlesManager {
    private var nextPage: Int = 1

    fun getArticles(subscribeOn: Scheduler = Schedulers.io(),
                    observeOn: Scheduler = AndroidSchedulers.mainThread(),
                    pageNumber: Int? = null): Flowable<List<Article>> {

        return NetworkManager.jsonClient
                .createService(SearchServices::class.java)
                .search(page = pageNumber ?: nextPage)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .flatMap {
                    val response = it.response ?: return@flatMap Flowable.fromArray(listOf<Article>())
                    val list = response.results ?: return@flatMap Flowable.fromArray(listOf<Article>())

                    //Note: Cache result list if needed
                    //val flowableCachedList = insert(list = list)

                    //Update next page number in memory
                    val pageInfo: PageInfo = response
                    nextPage = (pageInfo.currentPage ?: 0) + 1

                    //Note: We should return flowableCachedList if we were caching everything
                    return@flatMap Flowable.fromArray(list)
                }
    }

    fun getNextPageArticles(subscribeOn: Scheduler = Schedulers.io(),
                            observeOn: Scheduler = AndroidSchedulers.mainThread()):
            Flowable<List<Article>> {
        return getArticles(subscribeOn, observeOn)
    }

    fun canLoadMore(): Boolean = (nextPage <= 10)

}
