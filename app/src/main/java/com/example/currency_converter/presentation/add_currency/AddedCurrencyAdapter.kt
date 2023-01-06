package com.example.currency_converter.presentation.add_currency

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currency_converter.domain.models.Currency
import com.example.currency_converter.utils.CurrencyDiffCallback

class AddedCurrencyAdapter(
    private val listener: AddedCurrencyViewHolder.OnCurrencyClickListener,
): ListAdapter<Currency, AddedCurrencyViewHolder>(CurrencyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedCurrencyViewHolder {
        return AddedCurrencyViewHolder.create(parent)
    }

    override fun onBindViewHolder(viewHolder: AddedCurrencyViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }
}