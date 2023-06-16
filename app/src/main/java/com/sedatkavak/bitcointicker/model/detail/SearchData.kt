package com.sedatkavak.bitcointicker.model.detail

import com.sedatkavak.bitcointicker.model.home.CryptoCurrency

data class SearchData(
    val cryptoCurrencyList: List<CryptoCurrency>,
    val totalCount: String
)