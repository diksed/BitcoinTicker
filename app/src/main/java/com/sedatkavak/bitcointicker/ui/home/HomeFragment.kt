package com.sedatkavak.bitcointicker.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import com.sedatkavak.bitcointicker.base.BaseFragment
import com.sedatkavak.bitcointicker.databinding.FragmentHomeBinding
import com.sedatkavak.bitcointicker.model.home.CryptoModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate
), ItemClickListener {

    override val viewModel by viewModels<HomeViewModel>()
    override lateinit var binding: FragmentHomeBinding
    private lateinit var cryptoAdapter: CryptoAdapter

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.searchCrypto(s.toString().toLowerCase(Locale.getDefault()))
        }
        override fun afterTextChanged(s: Editable?) {}
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateFinished() {
        initAdapter()
        initRecyclerView()
    }
    override fun initializeListeners() {
        with(binding) {
            retryButtonHomeFragment.setOnClickListener {
                cryptoAdapter.retry()
            }
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    viewModel.searchCrypto(s.toString())
                }
                override fun afterTextChanged(s: Editable) {}
            })
        }
    }
    override fun observeEvents() {
        viewModel.cryptoResponse.observe(viewLifecycleOwner) {
            cryptoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
    private fun initAdapter() {
        cryptoAdapter = CryptoAdapter(this)
        cryptoAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBarHomeFragment.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerViewHomeFragment.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                retryButtonHomeFragment.isVisible = loadState.source.refresh is LoadState.Error
                errorTextHomeFragment.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.Error) {
                    errorTextHomeFragment.text =
                        (loadState.source.refresh as LoadState.Error).error.localizedMessage
                }
            }
        }
    }
    private fun initRecyclerView() {
        binding.recyclerViewHomeFragment.apply {
            setHasFixedSize(true)
            this.adapter =
                cryptoAdapter.withLoadStateFooter(footer = CryptoLoadStateAdapter { cryptoAdapter.retry() })
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        binding.searchEditText.addTextChangedListener(textWatcher)
    }
    override fun onItemClick(
        coin: CryptoModel, imageView: ImageView, titleTextView: TextView, symbolTextView: TextView
    ) {
        if (coin.symbol != null) {
            val extras = FragmentNavigatorExtras(
                imageView to "image${coin.symbol}",
                titleTextView to "title${coin.symbol}",
                symbolTextView to "symbol${coin.symbol}"
            )
            val navigation = HomeFragmentDirections.actionHomeFragmentToDetailFragment(coin)
            Navigation.findNavController(requireView()).navigate(navigation, extras)
        }
    }
}
