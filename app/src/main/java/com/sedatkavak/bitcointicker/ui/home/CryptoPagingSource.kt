package com.sedatkavak.bitcointicker.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.sedatkavak.bitcointicker.model.home.CryptoModel
import com.sedatkavak.bitcointicker.network.ApiFactory
import com.sedatkavak.bitcointicker.utils.Constants
import kotlinx.coroutines.delay
import java.io.IOException

class CryptoPagingSource(
    private val apiFactory: ApiFactory,
    private val keyword: String,
) : PagingSource<Int, CryptoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoModel> {
        val position = params.key ?: Constants.STARTING_PAGE_INDEX

        return try {
            val offsetForPagination = (position - 1) * 20 + 1
            val response = apiFactory.getData(params.loadSize, offsetForPagination)
            val cryptoModels = response.data
            val filteredCryptoModels = if (keyword.isBlank()) {
                cryptoModels
            } else {
                cryptoModels.filter { it.name?.contains(keyword, ignoreCase = true) == true }
            }
            LoadResult.Page(
                data = filteredCryptoModels,
                prevKey = if (position == Constants.STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (filteredCryptoModels.isNotEmpty()) position + 1 else null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            when(exception.response.code) {
                429 -> {
                    delay(5000)
                    load(params)  // retry
                }
                else -> LoadResult.Error(exception)
            }
        }
    }
    override fun getRefreshKey(state: PagingState<Int, CryptoModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}