package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*
import com.example.myapplication.TimesheetEntryAdapter

class AllWork : AppCompatActivity() {


    private lateinit var backButton: ImageButton
    private lateinit var adapter: TimesheetEntryAdapter
    private lateinit var databaseReference: DatabaseReference





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_work)



        val entriesRecyclerView = findViewById<RecyclerView>(R.id.entriesRecyclerView)

        //database
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app").reference.child("timesheet_entries")


        //initilise and set adapter
        adapter = TimesheetEntryAdapter()
        entriesRecyclerView.adapter = adapter
        entriesRecyclerView.layoutManager = LinearLayoutManager(this)

        //fetch data from database
        fetchDataFromFirebase()

        backButton = findViewById(R.id.backBTN)
        backButton.setOnClickListener{(back())}





    }
    private fun fetchDataFromFirebase() {
        // Attach a ValueEventListener to the database reference
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timesheetEntries = mutableListOf<TimesheetEntry>()

                // Iterate through the data in the snapshot and add it to the list
                for (entrySnapshot in snapshot.children) {
                    val entry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    entry?.let {
                        timesheetEntries.add(it)
                        Log.d("FirebaseData", "Fetched entry: $it")
                    }
                }

                // Update the RecyclerView adapter with the new list of entries
                runOnUiThread {
                    if(timesheetEntries.isEmpty())
                    {
                        // Display a message indicating there is no data
                        val textViewNoData = findViewById<TextView>(R.id.textViewNoData)
                        textViewNoData.visibility = View.VISIBLE

                    }else
                    {
                        // Hide the message if there is data
                        val textViewNoData = findViewById<TextView>(R.id.textViewNoData)
                        textViewNoData.visibility = View.GONE
                        adapter.submitList(timesheetEntries)

                    }
                    Log.d("FirebaseData", "Data updated. Entries count: ${timesheetEntries.size}")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                Log.e("FirebaseData", "Error fetching data: ${error.message}")
            }
        })
    }
    /*
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
        }*/

    /*private fun getSavedTimesheetEntries(): List<TimesheetEntry> {
        val timesheetEntries = mutableListOf<TimesheetEntry>()

        val databaseReference = FirebaseDatabase.getInstance().reference.child("timesheet_entries")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (entrySnapshot in snapshot.children) {
                    val entry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    entry?.let {
                        timesheetEntries.add(it)
                        Log.d(TAG, "Fetched entry: $it")
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error, if needed
                Log.e(TAG, "Failed to read value.", error.toException())
            }
        })

        return timesheetEntries
    }*/

    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }
}


