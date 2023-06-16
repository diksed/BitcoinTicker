package com.sedatkavak.bitcointicker.model.home

import com.sedatkavak.bitcointicker.model.detail.SearchData

data class SearchModel(
    val `data`: SearchData,
    val status: Status
)