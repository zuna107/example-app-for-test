package com.mysima.home.model

data class MetricItem(
    val id: String,
    val title: String,
    val value: String,
    val change: String,
    val isPositive: Boolean = true
)
