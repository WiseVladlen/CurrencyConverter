package com.example.currency_converter.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.R
import com.example.currency_converter.databinding.FragmentSettingsBottomSheetBinding
import com.example.currency_converter.utils.BASE_CURRENCY_BUNDLE_KEY
import com.example.currency_converter.utils.BASE_CURRENCY_REQUEST_KEY
import com.example.currency_converter.utils.showCurrencySelectionBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentSettingsBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenuItems()
    }

    private fun setupMenuItems() {
        binding.itemChangeBaseCurrency.apply {
            text = resources.getString(R.string.title_base_currency, "RUB")

            setOnClickListener {
                dismiss()
                parentFragmentManager.showCurrencySelectionBottomSheetFragment(
                    BASE_CURRENCY_REQUEST_KEY,
                    BASE_CURRENCY_BUNDLE_KEY,
                )
            }
        }
    }

    companion object {
        val TAG: String = SettingsBottomSheetFragment::class.java.simpleName
    }
}