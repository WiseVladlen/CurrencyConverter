package com.example.currency_converter.presentation.converter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.currency_converter.R
import com.example.currency_converter.databinding.FragmentConverterBinding
import com.example.currency_converter.utils.*
import com.example.currency_converter.utils.line_chart.ExchangeRate
import com.example.currency_converter.utils.line_chart.LineChartField
import java.util.*

class ConverterFragment : Fragment(R.layout.fragment_converter) {

    private val binding by viewBinding(FragmentConverterBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(SOURCE_CURRENCY_REQUEST_KEY) { _, bundle ->
            bundle.getString(SOURCE_CURRENCY_BUNDLE_KEY).let { result ->
                binding.buttonFrom.text = result
            }
        }

        setFragmentResultListener(TARGET_CURRENCY_REQUEST_KEY) { _, bundle ->
            bundle.getString(TARGET_CURRENCY_BUNDLE_KEY).let { result ->
                binding.buttonTo.text = result
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    private fun initializeViews() {
        setupOnClickListeners()
        setupChipGroup()
    }

    private fun setupOnClickListeners() {
        with(binding) {
            buttonFrom.setOnClickListener {
                parentFragmentManager.showCurrencySelectionBottomSheetFragment(
                    SOURCE_CURRENCY_REQUEST_KEY,
                    SOURCE_CURRENCY_BUNDLE_KEY,
                )
            }

            buttonTo.setOnClickListener {
                parentFragmentManager.showCurrencySelectionBottomSheetFragment(
                    TARGET_CURRENCY_REQUEST_KEY,
                    TARGET_CURRENCY_BUNDLE_KEY,
                )
            }
        }
    }

    private fun setupChipGroup() {
        binding.chipGroup.apply {
            setOnCheckedStateChangeListener { _, checkedIds ->
                when (checkedIds.first()) {
                    R.id.chip_1_week -> {
                        updateLineChart(preInit(1, Calendar.WEEK_OF_YEAR), DATE_FORMAT_WITH_WEEK)
                    }
                    R.id.chip_1_month -> {
                        updateLineChart(preInit(1, Calendar.MONTH), DATE_FORMAT_WITH_WEEK)
                    }
                    R.id.chip_6_months -> {
                        updateLineChart(preInit(6, Calendar.MONTH, 3), DATE_FORMAT_WITH_WEEK)
                    }
                    R.id.chip_1_year -> {
                        updateLineChart(preInit(1, Calendar.YEAR, 5), DATE_FORMAT_WITH_WEEK)
                    }
                    R.id.chip_5_years -> {
                        updateLineChart(preInit(5, Calendar.YEAR, 7), DATE_FORMAT_WITH_YEAR)
                    }
                }
            }

            check(R.id.chip_1_week)
        }
    }

    private fun updateLineChart(rates: List<ExchangeRate>, dateFormatPattern: String) {
        binding.lineChart.apply {
            lineChartField = LineChartField(rates, dateFormatPattern)
            actionListener = { marker, field ->
                field.setSelectedMarker(marker)
            }
        }
    }

    private fun preInit(cnt: Int, field: Int, amount: Int = 1): List<ExchangeRate> {
        val rates: MutableList<ExchangeRate> = mutableListOf()

        val date = Calendar.getInstance()

        val endDate = date.clone() as Calendar
        val startDate = date.clone() as Calendar

        startDate.add(field, -cnt)
        startDate.add(Calendar.DATE, (calculateDaysDifference(startDate, endDate) % amount).toInt())

        while (startDate.timeInMillis <= endDate.timeInMillis) {
            rates.add(ExchangeRate(60 + (75 - 60) * Random().nextDouble(), startDate.time))
            startDate.add(Calendar.DATE, amount)
        }
        return rates
    }
}