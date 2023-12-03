package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class Graphs : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        // Initialize the BarChart
        barChart = findViewById(R.id.barChart)
        setupBarChart()

        // Initialize the Firebase Database reference
        databaseReference =
            FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app/").reference

        // Fetch and display data when the activity is created
        fetchDataFromDatabase()

        val backButton = findViewById<ImageButton>(R.id.btnBack)

        backButton.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupBarChart() {
        barChart.description.isEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(false)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawGridLines(false)

        val yAxisRight = barChart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.isEnabled = false
    }

    private fun fetchDataFromDatabase() {
        val databaseNode = "timesheet_entries"

        databaseReference.child(databaseNode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("FirebaseData", "Number of entries: ${snapshot.childrenCount}")

                    val categoryHoursMap = mutableMapOf<String, Float>()

                    for (entrySnapshot in snapshot.children) {
                        val entry = entrySnapshot.getValue(TimesheetEntry::class.java)
                        if (entry != null) {
                            val categoryName = entry.categoryName
                            val startTime = entry.startTime
                            val endTime = entry.endTime

                            if (categoryName != null && startTime != null && endTime != null) {
                                val hours = calculateHours(startTime, endTime)
                                categoryHoursMap.merge(
                                    categoryName,
                                    hours
                                ) { existing, new -> existing + new }
                            }
                        }
                    }

                    displayCategoryHoursChart(categoryHoursMap)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error, if needed
                    Log.e("FirebaseData", "Error fetching data: ${error.message}")
                }
            })
    }

    private fun displayCategoryHoursChart(categoryHoursMap: Map<String, Float>) {
        val barEntries = categoryHoursMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value)
        }

        val dataSet = BarDataSet(barEntries, "Category Hours")
        dataSet.colors = mutableListOf(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            // Add more colors as needed
        )

        val data = BarData(dataSet)
        barChart.data = data

        barChart.invalidate()
    }

    private fun calculateHours(startTime: String?, endTime: String?): Float {
        Log.d("Graphs", "calculateHours: startTime=$startTime, endTime=$endTime")
        // Check if either startTime or endTime is null
        if (startTime == null || endTime == null) {
            return 0.0f
        }

        // Define the time format
        val format = SimpleDateFormat("HH:mm")

        try {
            // Parse startTime and endTime to Date objects
            val startDate = format.parse(startTime)
            val endDate = format.parse(endTime)

            // Calculate the duration between start and end in minutes
            val durationInMillis = endDate.time - startDate.time
            val durationInMinutes = durationInMillis / (1000 * 60)

            // Convert duration to hours (as a float)
            return durationInMinutes.toFloat() / 60
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0.0f
    }
}

