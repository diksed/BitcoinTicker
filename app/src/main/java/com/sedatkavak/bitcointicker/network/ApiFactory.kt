package com.sedatkavak.bitcointicker.network

import com.sedatkavak.bitcointicker.model.detail.DetailResponse
import com.sedatkavak.bitcointicker.model.detail.FavoriteModel
import com.sedatkavak.bitcointicker.model.home.CryptoModel
import com.sedatkavak.bitcointicker.model.home.CryptoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFactory {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getData(
        @Query("limit") limit: Int,
        @Query("start") start: Int,
        @Query("name") name: String? = null
    ): CryptoResponse

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getMarketData(): Response<FavoriteModel>

    @GET("v2/cryptocurrency/info")
    suspend fun getDetail(
        @Query("symbol") symbol: String
    ): DetailResponse
}
