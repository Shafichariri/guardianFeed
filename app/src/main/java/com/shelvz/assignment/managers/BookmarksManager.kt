package com.shelvz.assignment.managers

import com.shelvz.assignment.models.Article
import com.shelvz.assignment.models.Optional
import com.shelvz.assignment.repositories.ArticleRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object BookmarksManager {

    fun get(): Flowable<List<Article>> {
        return ArticleRepository.get()
    }
    
    fun get(id: String): Flowable<Optional<Article>> {
        return ArticleRepository.get(articleId = id)
    }

    fun add(article: Article, subscribeOn: Scheduler = Schedulers.io(),
            observeOn: Scheduler = AndroidSchedulers.mainThread()): Flowable<Boolean> {
        return ArticleRepository
                .insert(subscribeOn = subscribeOn, observeOn = observeOn, list = listOf(article))
                .map { it.isNotEmpty() }
                .onErrorReturn {
                    it.printStackTrace()
                    false
                }
    }

    fun remove(article: Article, subscribeOn: Scheduler = Schedulers.io(),
               observeOn: Scheduler = AndroidSchedulers.mainThread()): Flowable<Boolean> {
        return ArticleRepository
                .delete(subscribeOn = subscribeOn, observeOn = observeOn, articleId = article.id)
                .map { true }
                .onErrorReturn {
                    it.printStackTrace()
                    false
                }
    }
}
