package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class Graphs : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        // Initialize the PieChart
        pieChart = findViewById(R.id.pieChart)
        setupPieChart()

        // Initialize the Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app/").reference

        // Fetch and display data when the activity is created
        fetchDataFromDatabase()

        val backButton = findViewById<ImageButton>(R.id.btnBack)

        backButton.setOnClickListener()
        {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupPieChart() {
        pieChart.description.isEnabled = true
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(android.R.color.white)
        pieChart.setTransparentCircleRadius(25f)
    }

    private fun fetchDataFromDatabase() {
        val databaseNode = "timesheet_entries"

        databaseReference.child(databaseNode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryHoursMap = mutableMapOf<String, Float>()

                // Iterate through the data in the snapshot and calculate total hours for each category
                for (entrySnapshot in snapshot.children) {
                    val entry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    if (entry != null && entry.categoryName != null && entry.startTime != null && entry.endTime != null) {
                        // Store the values in local variables to avoid smart cast issues
                        val startTime = entry.startTime
                        val endTime = entry.endTime

                       // val hours = calculateHours(startTime, endTime)
                        //categoryHoursMap.merge(entry.categoryName, hours) { existing, new -> existing + new }
                    }
                }

                // Update the PieChart with the fetched data
                displayCategoryHoursChart(categoryHoursMap)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error, if needed
                Log.e("FirebaseData", "Error fetching data: ${error.message}")
            }
        })
    }

    private fun displayCategoryHoursChart(categoryHoursMap: Map<String, Float>) {
        // Convert the map entries to PieEntries
        val pieEntries = categoryHoursMap.entries.map { PieEntry(it.value, it.key) }

        // Create a PieDataSet
        val dataSet = PieDataSet(pieEntries, "Category Hours")
        dataSet.colors = mutableListOf(android.R.color.holo_red_light, android.R.color.holo_blue_light)

        // Create a PieData and set it to the PieChart
        val data = PieData(dataSet)
        pieChart.data = data

        // Refresh the chart
        pieChart.invalidate()
    }

    private fun calculateHours(startTime: String, endTime: String): Float {
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
