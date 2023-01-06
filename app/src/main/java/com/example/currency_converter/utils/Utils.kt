package com.example.currency_converter.utils

import com.example.currency_converter.utils.Constants.BASE
import com.example.currency_converter.utils.Constants.DOT
import com.example.currency_converter.utils.Constants.NUMBER_OF_DECIMAL_PLACES
import com.example.currency_converter.utils.Constants.PATTERN_REMOVE_LEADING_ZEROS
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.*

private object Constants {
    const val DOT = "."
    const val BASE = 10.0
    const val NUMBER_OF_DECIMAL_PLACES = 2
    const val PATTERN_REMOVE_LEADING_ZEROS = "(^0+)"
}

private fun calculateLengthFractionalPart(value: Double): Int {
    val fractionalPart = value.toString().split(DOT)[1]

    return if (fractionalPart.length == 1 || value >= BASE) {
        NUMBER_OF_DECIMAL_PLACES
    } else {
        val splitter = fractionalPart.split(PATTERN_REMOVE_LEADING_ZEROS.toRegex())
        return when (splitter.size) {
            1 -> NUMBER_OF_DECIMAL_PLACES
            else -> {
                val leadingZeros = fractionalPart.length - splitter[1].length
                leadingZeros + NUMBER_OF_DECIMAL_PLACES
            }
        }
    }
}

fun calculateDigitByValue(value: Double): Double {
    return if (value < 0) {
        BASE.pow(-calculateLengthFractionalPart(value))
    } else {
        val numberLength = ceil(log10(value)).toInt()
        val boundaryValue = BASE.pow(numberLength)
        val step = BASE.pow(2)
        var degree = 1.0
        while (degree * step < boundaryValue) {
            degree *= step
        }
        sqrt(degree)
    }
}

fun Double.toFormattedString(): String {
    return String.format("%.${calculateLengthFractionalPart(this)}f", this)
}

fun Double.toRoundedUp(): Double {
    return if (this < 0) {
        val context = MathContext(calculateLengthFractionalPart(this) + 1, RoundingMode.HALF_UP)
        BigDecimal(this, context).toDouble()
    } else {
        val digit = calculateDigitByValue(this)
        ceil(this / digit) * digit
    }
}

fun Double.toRoundedDown(): Double {
    return if (this < 0) {
        val context = MathContext(calculateLengthFractionalPart(this) + 1, RoundingMode.HALF_DOWN)
        BigDecimal(this, context).toDouble()
    } else {
        val digit = calculateDigitByValue(this)
        floor(this / digit) * digit
    }
}


fun Date.toSpecialDateFormat(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

fun calculateDaysDifference(startDate: Calendar, endDate: Calendar): Long {
    val timeDifference = abs(endDate.timeInMillis - startDate.timeInMillis)
    return TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS)
}