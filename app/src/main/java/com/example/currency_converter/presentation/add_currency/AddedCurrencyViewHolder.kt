package com.example.currency_converter.presentation.add_currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.R
import com.example.currency_converter.databinding.CurrencyLayoutBinding
import com.example.currency_converter.domain.models.Currency

class AddedCurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding<CurrencyLayoutBinding>()

    fun bind(currency: Currency, listener: OnCurrencyClickListener) {
        with(binding) {
            imageViewIcon.setImageResource(R.drawable.ic_search)
            textViewCurrencyName.text = currency.name
            textViewCurrencyCode.text = currency.code

            root.setOnClickListener {
                listener.onClick(currency)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): AddedCurrencyViewHolder {
            return AddedCurrencyViewHolder(
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