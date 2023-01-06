package com.example.currency_converter.utils

import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.example.currency_converter.presentation.add_currency.AddCurrencyBottomSheetFragment
import com.example.currency_converter.presentation.select_currency.SelectCurrencyBottomSheetFragment
import com.example.currency_converter.presentation.more_info.MoreInfoBottomSheetFragment
import com.example.currency_converter.presentation.settings.SettingsBottomSheetFragment

fun FragmentManager.showCurrencySelectionBottomSheetFragment(
    requestKey: String,
    bundleKey: String,
) {
    SelectCurrencyBottomSheetFragment().apply {
        arguments = bundleOf(
            CURRENCY_SELECTION_REQUEST_KEY to requestKey,
            CURRENCY_SELECTION_BUNDLE_KEY to bundleKey,
        )
    }.show(this, SelectCurrencyBottomSheetFragment.TAG)
}

fun FragmentManager.showAddingCurrencyBottomSheetFragment() {
    AddCurrencyBottomSheetFragment().show(this, AddCurrencyBottomSheetFragment.TAG)
}

fun FragmentManager.showMoreInfoBottomSheetFragment() {
    MoreInfoBottomSheetFragment().show(this, MoreInfoBottomSheetFragment.TAG)
}

fun FragmentManager.showSettingsBottomSheetFragment() {
    SettingsBottomSheetFragment().show(this, SettingsBottomSheetFragment.TAG)
}