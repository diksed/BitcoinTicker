package com.sedatkavak.bitcointicker.utils

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.sedatkavak.bitcointicker.BuildConfig
import com.sedatkavak.bitcointicker.di.BitcoinTicker.Companion.getAppContext

fun ImageView.loadImage(id: String?) = this.load(BuildConfig.BASE_IMAGE_URL.plus("$id.png")) {
    crossfade(true)
    crossfade(500)
    placeholder(createPlaceHolder())
}

private fun createPlaceHolder() = CircularProgressDrawable(getAppContext()).apply {
    strokeWidth = 12f
    centerRadius = 40f
    start()
}