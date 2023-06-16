package com.sedatkavak.bitcointicker.ui.account.profile

import androidx.lifecycle.ViewModel
import com.sedatkavak.bitcointicker.model.home.CryptoCurrency

class ProfileViewModel : ViewModel() {
    var savedListItem: ArrayList<CryptoCurrency> = ArrayList()
}