package com.sedatkavak.bitcointicker.ui.account.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sedatkavak.bitcointicker.databinding.FragmentProfileBinding
import com.sedatkavak.bitcointicker.model.home.CryptoCurrency
import com.sedatkavak.bitcointicker.network.ApiFactory
import com.sedatkavak.bitcointicker.network.ApiUtilities
import com.sedatkavak.bitcointicker.ui.account.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private lateinit var savedList: ArrayList<String>
    private lateinit var savedListItem: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
        readData()
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiFactory::class.java).getMarketData()

            if (res.body()!= null){
                withContext(Dispatchers.Main){
                    savedListItem = ArrayList()
                    savedListItem.clear()

                    for (savedData in savedList){
                        for (item in res.body()!!.data.cryptoCurrencyList){
                            if (savedData == item.symbol){
                                savedListItem.add(item)
                            }
                        }
                    }
                    binding.watchlistRecyclerView.adapter = FavoriteAdapter(requireContext(), savedListItem, "savedFragment")
                }
            }
        }

        return binding.root
    }
    private fun readData() {
        val sharedPreferences =
            requireContext().getSharedPreferences("savedList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("savedList", ArrayList<String>().toString())

        val type = object : TypeToken<ArrayList<String>>() {}.type

        savedList = gson.fromJson(json, type)
    }

    private fun logoutUser() {
        mAuth.signOut()
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
