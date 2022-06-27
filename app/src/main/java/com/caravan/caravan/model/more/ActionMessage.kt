package com.caravan.caravan.model.more

data class ActionMessage(
    val status: Boolean,
    val title: String? = null,
    val message: String? = null
)
