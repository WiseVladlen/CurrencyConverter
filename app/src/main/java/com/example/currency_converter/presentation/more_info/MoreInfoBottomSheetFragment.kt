package com.example.currency_converter.presentation.more_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.databinding.FragmentMoreInfoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoreInfoBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentMoreInfoBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenuItems()
    }

    private fun setupMenuItems() {
        with(binding) {
            itemDeleteCurrency.setOnClickListener {
                Toast.makeText(context, "Валюта удалена", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            itemChangeDefaultCurrency.setOnClickListener {
                Toast.makeText(context, "Валюта по умолчанию изменена", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    companion object {
        val TAG: String = MoreInfoBottomSheetFragment::class.java.simpleName
    }
}