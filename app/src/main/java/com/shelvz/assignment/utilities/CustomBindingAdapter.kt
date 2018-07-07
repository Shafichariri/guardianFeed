package com.shelvz.assignment.utilities

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class CustomBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = *arrayOf("imageUrl", "fit", "placeholder"), requireAll = false)
        fun setImageUrl(imageView: ImageView, url: String?, fit: Boolean?, placeholder: Drawable?) {
            if (url.isNullOrBlank()) {
                if (placeholder != null) {
                    imageView.setImageDrawable(placeholder)
                }
                return
            }

            val picasso = Picasso.get()
            val requestCreator: RequestCreator = picasso.load(url)
            if (fit == true) {
                requestCreator.fit()
            }
            if (placeholder != null) {
                requestCreator.placeholder(placeholder)
            }
            requestCreator.into(imageView)
        }
    }
}
