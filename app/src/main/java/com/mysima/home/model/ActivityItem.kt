package com.mysima.home.model

enum class ActivityStatus {
    COMPLETED,
    IN_PROGRESS,
    PENDING
}

data class ActivityItem(
    val id: String,
    val title: String,
    val user: String,
    val timestamp: String,
    val amountOrDetail: String,
    val status: ActivityStatus
)
