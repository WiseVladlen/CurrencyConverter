package com.example.currency_converter.domain.models

data class Currency(
    val id: Int,
    val name: String,
    val code: String,
    var isSelected: Boolean = false,
)