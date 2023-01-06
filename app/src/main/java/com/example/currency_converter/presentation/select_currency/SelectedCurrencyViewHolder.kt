package com.example.currency_converter.presentation.select_currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.R
import com.example.currency_converter.databinding.CurrencyLayoutBinding
import com.example.currency_converter.domain.models.Currency

class SelectedCurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding<CurrencyLayoutBinding>()

    fun bind(currency: Currency, listener: OnCurrencyClickListener) {
        with(binding) {
            textViewCurrencyName.text = currency.name
            textViewCurrencyCode.text = currency.code

            if (currency.isSelected) {
                imageViewIcon.setImageResource(R.drawable.ic_select)
            } else {
                imageViewIcon.setImageDrawable(null)
            }

            root.setOnClickListener {
                listener.onClick(currency)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SelectedCurrencyViewHolder {
            return SelectedCurrencyViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.currency_layout, parent, false)
            )
        }
    }

    fun interface OnCurrencyClickListener {
        fun onClick(currency: Currency)
    }
}