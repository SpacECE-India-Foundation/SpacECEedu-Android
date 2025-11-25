package com.spacece.milestonetracker.utils

fun parseDateToTimestamp(date: String?): Long {
    if (date.isNullOrBlank()) return 0L

    return try {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val parsed = sdf.parse(date)
        parsed?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}
