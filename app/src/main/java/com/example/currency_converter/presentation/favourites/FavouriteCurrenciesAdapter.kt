package com.example.currency_converter.presentation.favourites

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currency_converter.domain.models.Currency
import com.example.currency_converter.utils.CurrencyDiffCallback

class FavouriteCurrenciesAdapter(
    private val listener: FavouriteCurrencyViewHolder.CurrencyViewListener,
): ListAdapter<Currency, FavouriteCurrencyViewHolder>(CurrencyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCurrencyViewHolder {
        return FavouriteCurrencyViewHolder.create(parent)
    }

    override fun onBindViewHolder(viewHolder: FavouriteCurrencyViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }
}