package com.example.currency_converter.presentation.favourites

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.domain.models.Currency
import com.example.currency_converter.R
import com.example.currency_converter.databinding.FragmentFavouriteCurrenciesBinding
import com.example.currency_converter.utils.*

class FavouriteCurrenciesFragment : Fragment(R.layout.fragment_favourite_currencies) {

    private val binding by viewBinding(FragmentFavouriteCurrenciesBinding::bind)

    private val currencyAdapter = FavouriteCurrenciesAdapter {
        parentFragmentManager.showMoreInfoBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFragmentResultListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    private fun setupFragmentResultListeners() {
        setFragmentResultListener(BASE_CURRENCY_REQUEST_KEY) { _, bundle ->
            bundle.getString(BASE_CURRENCY_BUNDLE_KEY).let { result ->
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }

        setFragmentResultListener(ADD_CURRENCY_REQUEST_KEY) { _, bundle ->
            bundle.getString(ADD_CURRENCY_BUNDLE_KEY).let { result ->
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeViews() {
        setupRecyclerView()
        setupToolbar()
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

        for (i in 0..15) {
            list.add(Currency(i, "Russian ruble #$i", "RUB #$i", true))
        }

        currencyAdapter.submitList(list)
    }

    private fun setupToolbar() {
        with(binding) {
            val popupMenu = PopupMenu(
                requireContext(),
                toolbarImageButtonMore,
                Gravity.END,
                R.attr.actionOverflowMenuStyle,
                R.style.PopupMenuStyle,
            ).apply {
                menuInflater.inflate(R.menu.main_menu, this.menu)

                setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener when (menuItem.itemId) {
                        R.id.item_add -> {
                            dismiss()
                            parentFragmentManager.showAddingCurrencyBottomSheetFragment()
                            true
                        }
                        R.id.item_settings -> {
                            dismiss()
                            parentFragmentManager.showSettingsBottomSheetFragment()
                            true
                        }
                        else -> false
                    }
                }
                setForceShowIcon(true)
            }

            toolbarImageButtonMore.setOnClickListener {
                popupMenu.show()
            }
        }
    }
}