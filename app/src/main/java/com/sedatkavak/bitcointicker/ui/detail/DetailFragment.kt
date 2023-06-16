package com.sedatkavak.bitcointicker.ui.detail

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sedatkavak.bitcointicker.R
import com.sedatkavak.bitcointicker.base.BaseFragment
import com.sedatkavak.bitcointicker.databinding.FragmentDetailBinding
import com.sedatkavak.bitcointicker.model.detail.CoinDetail
import com.sedatkavak.bitcointicker.model.detail.DetailResponse
import com.sedatkavak.bitcointicker.model.home.CryptoCurrency
import com.sedatkavak.bitcointicker.model.home.CryptoModel
import com.sedatkavak.bitcointicker.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(
    FragmentDetailBinding::inflate
) {
    override val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var detailChartWebView: WebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backStackButton: ImageButton = view.findViewById(R.id.backStackButton)
        backStackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        detailChartWebView = view.findViewById(R.id.detailChartWebView)
        val webSettings = detailChartWebView.settings
        webSettings.javaScriptEnabled = true
        detailChartWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                detailChartWebView.visibility = View.VISIBLE
            }
        }
        args.coin.symbol?.let {
            viewModel.getDetail(it)
        }

        args.coin?.let {
            addToSaved(it)
        }

        val symbol = args.coin.symbol
        val url = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc/UTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utmsource=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        detailChartWebView.loadUrl(url)
    }
    var savedList: ArrayList<String>? = null
    var savedListIsChecked = false
    private fun addToSaved(data: CryptoModel) {
        readData()
        savedListIsChecked = if (savedList!!.contains(data.symbol)) {
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        } else {
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }
        binding.addWatchlistButton.setOnClickListener {
            savedListIsChecked = if (!savedListIsChecked) {
                if (!savedList!!.contains(data.symbol)) {
                    savedList!!.add(data.symbol!!)
                }
                storeData()
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                true
            } else {
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                savedList!!.remove(data.symbol)
                storeData()
                false
            }
        }
    }


    private fun storeData() {
        val sharedPreferences =
            requireContext().getSharedPreferences("savedList", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(savedList)
        editor.putString("savedList", json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences =
            requireContext().getSharedPreferences("savedList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("savedList", ArrayList<String>().toString())

        val type = object : TypeToken<ArrayList<String>>() {}.type

        savedList = gson.fromJson(json, type)
    }

    override fun onCreateFinished() {
        initTransitions()
        initViews()
        args.coin.symbol?.let {
            viewModel.getDetail(it)
        }
    }

    private fun initViews() = with(binding) {
        imageViewDetailFragment.loadImage(args.coin.id.toString())
        titleTextDetailFragment.text = args.coin.name
        symbolTextDetailFragment.text = "(${args.coin.symbol})"
        currentPriceTextDetailFragment.text =
            "Güncel Fiyat: ${String.format("%.2f", args.coin.quote?.uSD?.price) + " $"}"
    }

    private fun initTransitions() {
        val symbol = args.coin.symbol

        ViewCompat.setTransitionName(binding.imageViewDetailFragment, "image$symbol")
        ViewCompat.setTransitionName(binding.titleTextDetailFragment, "title$symbol")
        ViewCompat.setTransitionName(binding.symbolTextDetailFragment, "symbol$symbol")
        ViewCompat.setTransitionName(binding.currentPriceTextDetailFragment, "currentPrice$symbol")

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun initializeListeners() {}

    override fun observeEvents() {
        with(viewModel) {
            detailResponse.observe(viewLifecycleOwner) {
                parseData(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                handleView(it)
            }
            onError.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun parseData(it: DetailResponse?) {
        val gson = Gson()
        val json = gson.toJson(it?.data)
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject[args.coin.symbol.toString()] as JSONArray
        val coin = gson.fromJson(jsonArray.getJSONObject(0).toString(), CoinDetail::class.java)
        coin?.let {
            with(binding) {
                descriptionTextDetailFragment.text = it.description
            }
        }
    }

    private fun handleView(isLoading: Boolean = false) {
        binding.descriptionTextDetailFragment.isVisible = !isLoading
    }
}
