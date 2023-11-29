package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class Date : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var categoryEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var totalTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Initialize UI elements
        backButton = findViewById(R.id.backButton)
        categoryEditText = findViewById(R.id.categoryEditText)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        calculateButton = findViewById(R.id.calculateButton)
        totalTimeTextView = findViewById(R.id.totalTimeTextView)


        backButton.setOnClickListener{(back())}

        calculateButton.setOnClickListener {
            // Handle the calculate button click
            calculateTotalTime()
        }
    }

    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }

    private fun calculateTotalTime() {
        // Retrieve user inputs
        val category = categoryEditText.text.toString()
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()

        // Calculate total duration
      //  val totalMillis = calculateTotalDuration(category, startDate, endDate)

        // Format and display the result
       // val formattedTime = formatMillisToHours(totalMillis)
       // val resultText = "Total time spent on $category from $startDate to $endDate: $formattedTime"
        //totalTimeTextView.text = resultText
        totalTimeTextView.visibility = View.VISIBLE
    }

  //  private fun calculateTotalDuration(category: String, startDate: String, endDate: String): Long {
      //  val matchingEntries = getMatchingTimesheetEntries(category, startDate, endDate)
       // return calculateTotalDurationMillis(matchingEntries)
    }

  //  private fun calculateTotalDurationMillis(entries: List<TimesheetEntry>): Long {
        // Calculate the total duration in milliseconds
      //  return entries.sumBy { entry ->
    //        calculateDurationMillis(entry.startTime, entry.endTime)
      //  }
 //   }

   // private fun getMatchingTimesheetEntries(category: String, startDate: String, endDate: String): List<TimesheetEntry> {

        //return TimesheetEntry.filter { entry ->
           // entry.date >= startDate && entry.date <= endDate && entry.category == category
       // }
   // }

    private fun calculateDurationMillis(startTime: List<TimesheetEntry>, endTime: String): Long {
        // Assuming 'startTime' and 'endTime' are in the format "HH:mm AM/PM"
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val startDateTime = dateFormat.parse(startTime.toString())
        val endDateTime = dateFormat.parse(endTime)

        // Calculate the duration in milliseconds
        return endDateTime.time - startDateTime.time
    }

    private fun formatMillisToHours(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }



