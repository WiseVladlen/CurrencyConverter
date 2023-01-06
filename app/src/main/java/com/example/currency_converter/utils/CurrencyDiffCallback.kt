package com.example.currency_converter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.currency_converter.domain.models.Currency

object CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {

    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }
}