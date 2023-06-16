package com.sedatkavak.bitcointicker.ui.detail

import com.sedatkavak.bitcointicker.base.BaseRepository
import com.sedatkavak.bitcointicker.network.ApiFactory
import javax.inject.Inject

class DetailRepository @Inject constructor(private val apiFactory: ApiFactory) : BaseRepository() {
    suspend fun getDetail(
        symbol: String
    ) = safeApiRequest {
        apiFactory.getDetail(symbol)
    }
}