package com.example.currency_converter.presentation.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.R
import com.example.currency_converter.databinding.FavouriteCurrencyLayoutBinding
import com.example.currency_converter.domain.models.Currency

class FavouriteCurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding<FavouriteCurrencyLayoutBinding>()

    fun bind(currency: Currency, listener: CurrencyViewListener) {
        with(binding) {
            textViewCurrencyName.text = currency.name
            textViewCurrencyCode.text = currency.code

            root.setOnClickListener {
                listener.onLayoutClick(currency)
            }

            imageButtonMoreInfo.setOnClickListener {
                listener.onMoreInfoButtonClick(currency)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): FavouriteCurrencyViewHolder {
            return FavouriteCurrencyViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.favourite_currency_layout, parent, false)
            )
        }
    }

    fun interface CurrencyViewListener {
        fun onLayoutClick(currency: Currency) { }
        fun onMoreInfoButtonClick(currency: Currency)
    }
}