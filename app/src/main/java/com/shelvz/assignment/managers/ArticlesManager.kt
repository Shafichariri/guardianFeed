package com.shelvz.assignment.managers

import com.shelvz.assignment.models.Article
import com.shelvz.assignment.models.Optional
import com.shelvz.assignment.models.PageInfo
import com.shelvz.assignment.network.NetworkManager
import com.shelvz.assignment.network.apiServices.SearchServices
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ArticlesManager {
    private var nextPage: Int? = 1
    private var list: MutableList<Article> = mutableListOf()

    fun addArticle(article: Article, cache: Boolean = false, 
                   subscribeOn: Scheduler = Schedulers.io(),
                   observeOn: Scheduler = AndroidSchedulers.mainThread()): Flowable<Boolean> {
        //Add to Memory
        list.add(article)

        return if (cache) {
            //Add to db
            BookmarksManager.add(subscribeOn = subscribeOn, observeOn = observeOn, article = article)
        } else {
            Flowable.just(true).subscribeOn(subscribeOn).observeOn(observeOn)
        }
    }

    fun removeArticle(article: Article, cache: Boolean = false,
                      subscribeOn: Scheduler = Schedulers.io(),
                      observeOn: Scheduler = AndroidSchedulers.mainThread()): Flowable<Boolean> {
        //Remove from Memory
        list.remove(article)

        return if (cache) {
            //Remove from db
            BookmarksManager.remove(subscribeOn = subscribeOn, observeOn = observeOn, article = article)
        } else {
            Flowable.just(true).subscribeOn(subscribeOn).observeOn(observeOn)
        }
    }

    fun getArticle(id: String, cache: Boolean = false): Flowable<Optional<Article>> {
        if (!cache) {
            //From Memory
            val article = list.find { it.id == id }
            if (article != null) {
                return Flowable.just(Optional(article))
            }
        }
        //From db
        return BookmarksManager.get(id = id)
    }

    fun getArticles(subscribeOn: Scheduler = Schedulers.io(),
                    observeOn: Scheduler = AndroidSchedulers.mainThread(),
                    pageNumber: Int? = null): Flowable<List<Article>> {

        return NetworkManager.jsonClient
                .createService(SearchServices::class.java)
                .search(page = pageNumber ?: nextPage ?: 1)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .flatMap {
                    val response = it.response ?: return@flatMap Flowable.fromArray(listOf<Article>())
                    val list = response.results ?: return@flatMap Flowable.fromArray(listOf<Article>())

                    //Update next page number in memory
                    val pageInfo: PageInfo = response
                    nextPage = if (pageInfo.currentPage == null) null
                    else ((pageInfo.currentPage ?: 0) + 1)

                    return@flatMap Flowable.fromArray(list)
                }
    }

    fun getNextPageArticles(subscribeOn: Scheduler = Schedulers.io(),
                            observeOn: Scheduler = AndroidSchedulers.mainThread()):
            Flowable<List<Article>> {
        return getArticles(subscribeOn, observeOn)
    }

    fun canLoadMore(): Boolean = (nextPage != null)

}
