package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class AllWork : AppCompatActivity() {


    private lateinit var backButton: ImageButton





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_work)

        val entriesRecyclerView = findViewById<RecyclerView>(R.id.entriesRecyclerView)

        // Retrieve the saved timesheet entries
        val timesheetEntries = getSavedTimesheetEntries()

        // Initialize and set the adapter for the RecyclerView
        val adapter = TimesheetEntryAdapter(timesheetEntries)
        entriesRecyclerView.adapter = adapter

        backButton = findViewById(R.id.backBTN)
        backButton.setOnClickListener{(back())}





    }

    private fun getSavedTimesheetEntries(): List<TimesheetEntry> {
        val sharedPreferences = getSharedPreferences("TimesheetEntries", Context.MODE_PRIVATE)
        val gson = Gson()
        val entriesSet = sharedPreferences.getStringSet("timesheet_entries", setOf()) ?: setOf()

        // Convert the entriesSet back to a list of TimesheetEntry
        val timesheetEntries = entriesSet.mapNotNull {
            try {
                gson.fromJson(it, TimesheetEntry::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return timesheetEntries
    }
    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }
}