package com.example.currency_converter.presentation.add_currency

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.databinding.FragmentCurrencyListBottomSheetBinding
import com.example.currency_converter.utils.ADD_CURRENCY_BUNDLE_KEY
import com.example.currency_converter.utils.ADD_CURRENCY_REQUEST_KEY
import com.example.currency_converter.domain.models.Currency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddCurrencyBottomSheetFragment: BottomSheetDialogFragment() {

    private val binding: FragmentCurrencyListBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    private val currencyAdapter = AddedCurrencyAdapter { currency ->
        setFragmentResult(
            ADD_CURRENCY_REQUEST_KEY,
            bundleOf(ADD_CURRENCY_BUNDLE_KEY to "Валюта ${currency.name} добавлена"),
        )
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.behavior.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun initializeViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.searchedCurrenciesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currencyAdapter
        }

        preInit()
    }

    private fun preInit() {
        val list = mutableListOf<Currency>()

        for (i in 0..50) {
            list.add(Currency(i, "Russian ruble #$i", "RUB #$i"))
        }

        currencyAdapter.submitList(list)
    }

    companion object {
        val TAG: String = AddCurrencyBottomSheetFragment::class.java.simpleName
    }
}