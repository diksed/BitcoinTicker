package com.sedatkavak.bitcointicker.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val cryptoResponse = currentQuery.switchMap { queryString ->
        repository.getData(queryString).cachedIn(viewModelScope)
    }
    companion object {
        private const val DEFAULT_QUERY = ""
    }
    fun searchCrypto(query: String) {
        currentQuery.value = query
    }
}
