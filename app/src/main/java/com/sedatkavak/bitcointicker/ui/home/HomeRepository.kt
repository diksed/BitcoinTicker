package com.sedatkavak.bitcointicker.ui.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.sedatkavak.bitcointicker.base.BaseRepository
import com.sedatkavak.bitcointicker.network.ApiFactory
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiFactory: ApiFactory) : BaseRepository() {

    fun getData(keyword: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CryptoPagingSource(apiFactory = apiFactory, keyword = keyword) }
    ).liveData
}