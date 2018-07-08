package com.shelvz.assignment.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.shelvz.assignment.R
import com.shelvz.assignment.activities.MainActivity.Companion.EXTRA_ARTICLE_ID
import com.shelvz.assignment.databinding.ActivityArticleDetailsBinding
import com.shelvz.assignment.kit.databinding.DataBoundActivity
import com.shelvz.assignment.viewModels.ArticleDetailsActivityViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ArticleDetailsActivity :
        DataBoundActivity<ActivityArticleDetailsBinding, ArticleDetailsActivityViewModel>() {


    override fun classForViewModel(): Class<ArticleDetailsActivityViewModel> {
        return ArticleDetailsActivityViewModel::class.java
    }

    override fun layoutForDataBinding(): Int {
        return R.layout.activity_article_details
    }

    override fun onCreated(viewDataBinding: ActivityArticleDetailsBinding, viewModel: ArticleDetailsActivityViewModel, savedInstanceState: Bundle?) {
        supportPostponeEnterTransition()
        executeTransition()
        //Binding listener
        viewDataBinding.listener = this

        val extras = intent.extras
        val articleId = extras.getString(EXTRA_ARTICLE_ID)

        viewModel.getArtileLiveData().observe(this, Observer { article ->
            val article = article ?: return@Observer
            viewDataBinding.description = article.webTitle?.capitalize()
            viewDataBinding.title = article.type.capitalize()
        })
        viewModel.loadArticle(articleId)
    }

    private fun executeTransition() {
        val extras = intent.extras
        val imageView = viewDataBinding.imageViewThumbnail
        val articleImageUrl = extras.getString(MainActivity.EXTRA_ARTICLE_IMAGE_URL)

//        Note: Set the Image transition name programmatically if it is not set in the xml         
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val transitionName = extras.getString(MainActivity.EXTRA_IMAGE_TRANSITION)
//            imageView.transitionName = transitionName
//        }

        Picasso.get()
                .load(articleImageUrl)
                .noFade()
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        supportStartPostponedEnterTransition()
                    }
                })
    }

    //region D A T A B O U N D  C L I C K  L I S T E N E R S    
    fun onBookmarkArticleClick(view: View) {
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        //Remove from memory
        viewModel.removeArticle()
    }
}
