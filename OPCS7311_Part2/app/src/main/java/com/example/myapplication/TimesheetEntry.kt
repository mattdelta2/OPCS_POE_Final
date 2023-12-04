package com.example.myapplication
import com.google.firebase.database.IgnoreExtraProperties


data class TimesheetEntry(
    var categoryName: String? = null,
    var date: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var nk_pevOnB_ewmEVqpjS: String? = null
) {
    // Add a default, no-argument constructor
    constructor() : this("", "", "", "", "", "", "")
}
