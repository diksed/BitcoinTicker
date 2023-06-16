package com.sedatkavak.bitcointicker.model.detail

import com.sedatkavak.bitcointicker.model.home.CryptoCurrency

data class FavoriteData(
    val cryptoCurrencyList: List<CryptoCurrency>,
    val totalCount: String
)