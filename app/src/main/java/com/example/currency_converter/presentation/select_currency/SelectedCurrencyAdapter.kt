package com.example.currency_converter.presentation.select_currency

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currency_converter.domain.models.Currency
import com.example.currency_converter.utils.CurrencyDiffCallback

class SelectedCurrencyAdapter(
    private val listener: SelectedCurrencyViewHolder.OnCurrencyClickListener,
): ListAdapter<Currency, SelectedCurrencyViewHolder>(CurrencyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedCurrencyViewHolder {
        return SelectedCurrencyViewHolder.create(parent)
    }

    override fun onBindViewHolder(viewHolder: SelectedCurrencyViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }
}