package com.sedatkavak.bitcointicker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sedatkavak.bitcointicker.databinding.RecyclerRowLayoutBinding
import com.sedatkavak.bitcointicker.model.home.CryptoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class CryptoAdapter(private val listener: ItemClickListener) :
    PagingDataAdapter<CryptoModel, CryptoAdapter.CryptoViewHolder>(CRYPTO_MODEL_DIFF_UTIL) {
    private var coroutineScope: CoroutineScope
    init {
        coroutineScope = CoroutineScope(Dispatchers.Main)
    }
    companion object {
        private val CRYPTO_MODEL_DIFF_UTIL = object : DiffUtil.ItemCallback<CryptoModel>() {
            override fun areItemsTheSame(oldItem: CryptoModel, newItem: CryptoModel): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: CryptoModel, newItem: CryptoModel): Boolean =
                oldItem == newItem
        }
    }
    class CryptoViewHolder(private val binding: RecyclerRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: CryptoModel, listener: ItemClickListener) {
            binding.coin = coin
            binding.onItemClickListener = listener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): CryptoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerRowLayoutBinding.inflate(layoutInflater, parent, false)
                return CryptoViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder =
        CryptoViewHolder.from(parent)
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let {
            holder.bind(it, listener)
        }
    }
}
