package com.caravan.caravan.model

data class AppLanguage(
    val id: String,
    val name: String,
    val flag: String,
    var isSelected: Boolean = false
)
