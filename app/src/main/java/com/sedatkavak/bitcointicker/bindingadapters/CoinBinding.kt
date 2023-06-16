package com.sedatkavak.bitcointicker.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sedatkavak.bitcointicker.utils.loadImage

@BindingAdapter("load_image")
fun loadImage(imageView: ImageView, id: String) {
    imageView.loadImage(id)
}