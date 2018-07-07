package com.shelvz.assignment.managers

import com.shelvz.assignment.models.Article
import com.shelvz.assignment.models.PageInfo
import com.shelvz.assignment.network.NetworkManager
import com.shelvz.assignment.network.apiServices.SearchServices
import com.shelvz.assignment.repositories.ArticleRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ArticlesManager {
    private var nextPage: Int? = 1
    private var list: MutableList<Article> = mutableListOf()

    fun addArticle(article: Article, cache: Boolean = false) {
        //Add to Memory
        list.add(article)
        
        if (cache) {
            //Add to db
            ArticleRepository
                    .insert(list = listOf(article))
                    .subscribe({ _ -> }, { t: Throwable? -> t?.printStackTrace() })
        }
    }

    fun removeArticle(article: Article, cache: Boolean = false) {
        //Remove from Memory
        list.remove(article)

        if (cache) {
            //Remove from db
            ArticleRepository.delete(articleId = article.id)
                    .subscribe({ _ -> }, { t: Throwable? -> t?.printStackTrace() })
        }
    }

    fun getArticle(id: String): Flowable<Article> {
        //From Memory
        val article = list.find { it.id == id }
        if (article != null) {
            return Flowable.just(article)
        }
        //From db
        return ArticleRepository.get(articleId = id)
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

                    //Note: Cache result list if needed
                    //val flowableCachedList = insert(list = list)

                    //Update next page number in memory
                    val pageInfo: PageInfo = response
                    nextPage = if (pageInfo.currentPage == null) null
                    else ((pageInfo.currentPage ?: 0) + 1)

                    //Note: We should return flowableCachedList if we were caching everything
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
