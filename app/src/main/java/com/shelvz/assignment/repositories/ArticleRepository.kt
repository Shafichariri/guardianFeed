package com.shelvz.assignment.repositories

import android.content.Context
import com.shelvz.assignment.kit.base.BaseApplication
import com.shelvz.assignment.models.Article
import com.shelvz.assignment.persistance.AppDatabase
import com.shelvz.assignment.persistance.dao.ArticleDao
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ArticleRepository : Repository<Article> {
    private val dao: ArticleDao
        get() = AppDatabase.getInstance(context = context).articleDao()

    override val context: Context
        get() = BaseApplication.getInstance()

    override fun delete() {
        //Run delete safely
        Flowable.fromCallable { instantDelete() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Thread-safe insert for a list of articles
     *
     * @param observeOn
     * @param subscribeOn
     * @param list of articles to insert
     * @return Flowable list of all inserted items
     * */
    fun insert(subscribeOn: Scheduler = Schedulers.io(),
               observeOn: Scheduler = AndroidSchedulers.mainThread(),
               list: List<Article>): Flowable<List<Article>> {
        return Flowable
                .fromCallable {
                    instantInsert(list)
                    return@fromCallable list
                }
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
    }

    /**
     * Delete all entries in db
     * (Not-Thread-Safe)
     * */
    private fun instantDelete() {
        dao.delete()
    }

    /**
     * Insert is done on the thread it is called from.
     * (Not-Thread-Safe)
     * Insert a list of articles
     *
     * @param list of articles to insert
     * */
    private fun instantInsert(list: List<Article>) {
        AppDatabase
                .getInstance(context = BaseApplication.getInstance())
                .articleDao()
                .insert(list)
    }

}
