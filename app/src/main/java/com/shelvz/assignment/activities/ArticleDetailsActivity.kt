package com.shelvz.assignment.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.shelvz.assignment.R
import com.shelvz.assignment.activities.MainActivity.Companion.EXTRA_ARTICLE_ID
import com.shelvz.assignment.databinding.ActivityArticleDetailsBinding
import com.shelvz.assignment.kit.databinding.DataBoundActivity
import com.shelvz.assignment.repositories.ArticleRepository.context
import com.shelvz.assignment.viewModels.ArticleDetailsActivityViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.lang.ref.WeakReference

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

        viewModel.getArticleLiveData().observe(this, Observer { article ->
            val article = article ?: return@Observer
            viewDataBinding.description = article.webTitle?.capitalize()
            viewDataBinding.title = article.type.capitalize()
            syncFabButton()
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

    private fun syncFabButton() {
        viewModel.isArticleBookmarked { success, bookmarked ->
            if (success) {
                updateFabButtonStatus(bookmarked)
            } else {
                //TODO: handle Error
            }
        }
    }
    
    private fun updateFabButtonStatus(bookmarked: Boolean) {
        val iconRes: Int = if (bookmarked) R.drawable.v_icon_bookmark_selected else R.drawable.v_icon_bookmark_empty
        val drawable = ContextCompat.getDrawable(context, iconRes)
        viewDataBinding?.bookmarkFab?.setImageDrawable(drawable)
    }

    //region D A T A B O U N D  C L I C K  L I S T E N E R S    
    fun onBookmarkArticleClick(view: View) {
        val weakRef = WeakReference(this)
        viewModel.bookmarkArticle { success, isBookmarked ->
            if (success) {
                weakRef.get()?.updateFabButtonStatus(bookmarked = isBookmarked)
            }
        }
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        //Remove from memory
        viewModel.removeArticle()
    }
}
