package com.morcinek.omise.core.extensions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.morcinek.omise.R

fun Fragment.loadImageWithProgressAndError(imageView: ImageView, url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(CircularProgressDrawable(requireContext()).apply {
            strokeWidth = 10f
            centerRadius = 48f
            start()
        })
        .error(R.drawable.ic_error)
        .into(imageView)
}
