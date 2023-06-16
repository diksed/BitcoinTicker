package com.sedatkavak.bitcointicker.ui.home

import android.widget.ImageView
import android.widget.TextView
import com.sedatkavak.bitcointicker.model.home.CryptoModel

interface ItemClickListener {
    fun onItemClick(
        coin: CryptoModel, imageView: ImageView, titleTextView: TextView, symbolTextView: TextView
    )
}