package com.shelvz.assignment.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.shelvz.assignment.kit.base.BaseEvent
import com.shelvz.assignment.kit.base.SyncStatus
import com.shelvz.assignment.kit.common.RxBus
import com.shelvz.assignment.managers.ArticlesManager
import com.shelvz.assignment.managers.LatestArticlesManager

class PullDataService : IntentService(TAG) {

    companion object {
        private val TAG = PullDataService::class.java.simpleName
        private const val EXTRA_DATE_FROM = "EXTRA_DATE_FROM"
        const val DEFAULT_PERIOD = 30

        fun getIntent(context: Context, dateFrom: String): Intent {
            val intent = Intent(context, PullDataService::class.java)
            intent.putExtra(EXTRA_DATE_FROM, dateFrom)
            return intent
        }
    }

    init {
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        RxBus.publish(BaseEvent.PullLatest(status = SyncStatus.Started()))

        val dateFrom: String = intent?.getStringExtra(EXTRA_DATE_FROM)
                ?: return RxBus.publish(BaseEvent.PullLatest(status = SyncStatus.Failed()))


        val latestArticles = ArticlesManager
                .getLatestArticles(pageSize = 100, dateFrom = dateFrom)
                .onErrorReturn {
                    it.printStackTrace()
                    //Note this indicates that API REQUEST FAILED
                    listOf()
                }
                .blockingLast()

        //Finish early if API result was empty
        if (latestArticles.isEmpty()) return RxBus.publish(BaseEvent.PullLatest(status = SyncStatus.Finished()))

        //Add temporary
        LatestArticlesManager.add(latestArticles)
        //Notify finished
        RxBus.publish(BaseEvent.PullLatest(status = SyncStatus.Finished()))
    }
}
