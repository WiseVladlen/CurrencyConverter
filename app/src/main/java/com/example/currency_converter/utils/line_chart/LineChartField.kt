package com.example.currency_converter.utils.line_chart

import com.example.currency_converter.utils.toRoundedDown
import com.example.currency_converter.utils.toRoundedUp
import java.util.*

typealias FieldChangedListener = (field: LineChartField) -> Unit

data class Position(
    var x: Float = 0f,
    var y: Float = 0f,
)

data class ExchangeRate(
    val value: Double,
    val date: Date,
)

data class Marker(
    val value: Double,
    val date: Date,
    val position: Position,
)

enum class DevelopmentType {
    REGRESS,
    PROGRESS,
    STAGNATION;
}

data class ChartParameters(
    val markers: List<Marker>,
    val xUnit: Float = 0f,
    val yUnit: Float = 0f,
    val firstMarkerPositionX: Float = 0f,
    val lastMarkerPositionX: Float = 0f,
    val topMarkerPositionY: Float = 0f,
    val bottomMarkerPositionY: Float = 0f,
)

data class LineChartField(
    val rates: List<ExchangeRate>,
    val dateFormatPattern: String,
    private val open: Double = rates.first().value,
    private val close: Double = rates.last().value,
    private val high: Double = rates.maxOf { it.value },
    private val low: Double = rates.minOf { it.value },
    val roundedHigh: Double = high.toRoundedUp(),
    val roundedLow: Double = low.toRoundedDown(),
    val roundedDifference: Double = roundedHigh - roundedLow,
) {
    val developmentType: DevelopmentType = when {
        open > close -> DevelopmentType.REGRESS
        open < close -> DevelopmentType.PROGRESS
        else -> DevelopmentType.STAGNATION
    }

    var listener: FieldChangedListener? = null

    var chartParameters: ChartParameters? = null

    private var _selectedMarker: Marker? = null
    val selectedMarker: Marker? get() = _selectedMarker

    fun setSelectedMarker(marker: Marker) {
        _selectedMarker = marker
        listener?.invoke(this)
    }
}