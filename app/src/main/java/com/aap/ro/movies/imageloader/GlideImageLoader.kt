package com.aap.ro.movies.imageloader

import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

class GlideImageLoader @Inject constructor(): Imageloader {
    override fun loadImageInto(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).into(imageView)
    }
}