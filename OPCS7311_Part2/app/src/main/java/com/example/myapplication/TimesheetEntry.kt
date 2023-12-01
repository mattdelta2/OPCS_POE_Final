package com.example.myapplication

data class TimesheetEntry(
    val categoryName: String? = null,
    val date: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
) {
    // Add a default, no-argument constructor
    constructor() : this("", "", "", "", "")
}
