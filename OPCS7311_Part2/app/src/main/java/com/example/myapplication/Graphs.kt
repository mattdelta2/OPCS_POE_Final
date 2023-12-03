package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class Graphs : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var databaseReference: DatabaseReference
    private lateinit var editTextDate: EditText
    private lateinit var textViewDailyGoals: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        // Initialize the BarChart
        barChart = findViewById(R.id.barChart)
        setupBarChart()

        // Initialize the Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app/").reference

        // Fetch and display data when the activity is created
        fetchDataFromDatabase()

        editTextDate = findViewById(R.id.EditText)
        textViewDailyGoals = findViewById(R.id.textViewDailyGoals)

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

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.textSize = 15f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // set the granularity to 1 to show one label per category

        xAxis.textSize = 20f

        yAxisLeft.axisMinimum = 0f // Set the minimum value on the Y-axis
        yAxisLeft.setLabelCount(6, true) // Set the number of labels on the Y-axis
        yAxisLeft.axisMaximum = 20f // Set the maximum value on the Y-axis
        yAxisLeft.setDrawLabels(true) // Show labels on the Y-axis

        // Add Y-axis label
        val yAxisLabel = YAxisLabelFormatter("Hours")
        yAxisLeft.valueFormatter = yAxisLabel

        val yAxisRight = barChart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.isEnabled = false
    }

    private fun fetchDataFromDatabase() {
        val databaseNode = "timesheet_entries"

        // Continue with your existing code for fetching timesheet_entries
        databaseReference.child(databaseNode).addListenerForSingleValueEvent(object : ValueEventListener {
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
                            categoryHoursMap.merge(categoryName, hours) { existing, new -> existing + new }
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

        // Define your custom colors for each bar
        val customColors = intArrayOf(
            0xFFE34234.toInt(),  // Vermilion
            0xFFE55D3F.toInt(),
            0xFFE7774A.toInt(),
            0xFFE99255.toInt(),
            0xFFEAAE60.toInt(),
            0xFFEBC96B.toInt(),
            0xFFEDD476.toInt(),
            0xFFEEEF81.toInt(),
            0xFFEFFF8C.toInt(),
            0xFFF0FF97.toInt()
        )

        val dataSet = BarDataSet(barEntries, "Category Hours")

        // Set custom colors for each bar
        dataSet.colors = customColors.toList()

        // Disable drawing values above bars
        dataSet.setDrawValues(false)

        val data = BarData(dataSet)

        // Set category names as labels on the X-axis
        val categoryNames = categoryHoursMap.keys.toTypedArray()
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = CategoryAxisValueFormatter(categoryNames)

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

    private fun updateGraph() {
        val chosenDate = editTextDate.text.toString()

        // Add logic to update the graph based on the chosen date
        // For example, you can fetch data for the selected date from the database
        // and update the graph accordingly.

        // After updating the graph, you can also fetch and display the daily goals for the chosen date
        fetchAndDisplayDailyGoals(chosenDate)
    }

    private fun fetchAndDisplayDailyGoals(chosenDate: String) {
        // Fetch daily goals for the chosen date from the database
        val databaseNode = "daily_goals"

        databaseReference.child(databaseNode).child(chosenDate).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dailyGoals = snapshot.getValue(Goals::class.java)

                    // Now you have the daily goals, you can use them as needed
                    // For example, you can display them in your UI or use them for calculations

                    if (dailyGoals != null) {
                        val maxGoal = dailyGoals.maxGoal
                        val minGoal = dailyGoals.minGoal

                        // Update your UI with the daily goals
                        textViewDailyGoals.text = "Max Goal: $maxGoal, Min Goal: $minGoal"
                    }
                } else {
                    // Handle the case where there are no daily goals for the chosen date
                    textViewDailyGoals.text = "No goals set for the chosen date."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error, if needed
                Log.e("FirebaseData", "Error fetching data: ${error.message}")
            }
        })
    }
}

class CategoryAxisValueFormatter(private val categories: Array<String>) : com.github.mikephil.charting.formatter.ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < categories.size) {
            categories[index]
        } else {
            ""
        }
    }
}

class YAxisLabelFormatter(private val label: String) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return "$value $label"  // Append the label to the value
    }
}
