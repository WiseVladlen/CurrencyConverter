package com.example.currency_converter.presentation.select_currency

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
import com.example.currency_converter.utils.CURRENCY_SELECTION_BUNDLE_KEY
import com.example.currency_converter.utils.CURRENCY_SELECTION_REQUEST_KEY
import com.example.currency_converter.domain.models.Currency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCurrencyBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentCurrencyListBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    private lateinit var requestKey: String
    private lateinit var bundleKey: String

    private val currencyAdapter = SelectedCurrencyAdapter { currency ->
        setFragmentResult(requestKey, bundleOf(bundleKey to currency.code))
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requestKey = arguments?.getString(CURRENCY_SELECTION_REQUEST_KEY) ?: String()
        bundleKey = arguments?.getString(CURRENCY_SELECTION_BUNDLE_KEY) ?: String()

        return binding.root
    }

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
            if (i == 20) {
                list.add(Currency(i, "Russian ruble #$i", "RUB #$i", true))
            } else {
                list.add(Currency(i, "Russian ruble #$i", "RUB #$i"))
            }
        }

        currencyAdapter.submitList(list)
    }

    companion object {
        val TAG: String = SelectCurrencyBottomSheetFragment::class.java.simpleName
    }
}