package com.sedatkavak.bitcointicker.ui.account.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sedatkavak.bitcointicker.R
import com.sedatkavak.bitcointicker.databinding.NewFavoriteItemBinding
import com.sedatkavak.bitcointicker.model.home.CryptoCurrency

class FavoriteAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = NewFavoriteItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.new_favorite_item, parent, false)
        )
    }

    fun updateData(dataItem: List<CryptoCurrency>) {
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol
        holder.binding.currencyPriceTextView.text =
            "Güncel Fiyat: \n${String.format("%.02f", item.quotes[0].price)} $"


        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}