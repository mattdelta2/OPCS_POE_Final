package com.example.myapplication

data class TimesheetEntry(
    var categoryName: String? = null, // Add a setter or field for 'category'
    var date: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var nk_pevOnB_ewmEVqpjS: String? = null // Replace dashes with underscores
) {
    // Add a default, no-argument constructor
    constructor() : this("", "", "", "", "", "")
}
