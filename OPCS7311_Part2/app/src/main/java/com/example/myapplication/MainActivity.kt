package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class CategoryName(val id: Int, val name: String)



class MainActivity : AppCompatActivity() {

    private lateinit var categoryManager: CategoryManager
    private lateinit var categoryContainer: LinearLayout
    private lateinit var addCategoryButton: Button
    private lateinit var categoryNameEditText: EditText
    private lateinit var entryDateEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText : EditText
    private lateinit var descriptionEditText: EditText
    private val SELECT_IMAGE_REQUEST_CODE = 1001

    private lateinit var backButton: ImageButton

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_LAUNCH = "first_launch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        categoryManager = CategoryManager(applicationContext)
        categoryContainer = findViewById(R.id.categoryContainer)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        categoryNameEditText = findViewById(R.id.categoryNameEditText)
        backButton = findViewById(R.id.btnBack)
        entryDateEditText = findViewById(R.id.entryDateEditText)
        startTimeEditText = findViewById(R.id.startTimeEditText)
        endTimeEditText = findViewById(R.id.endTimeEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)





        // In onCreate method
        val categoryImageView = findViewById<ImageView>(R.id.userImage)

        val categories = categoryManager.getCategories().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



        addCategoryButton.setOnClickListener {
            val date = entryDateEditText.text.toString()
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()

            val userImage = categoryManager.selectImageForCategory(this, SELECT_IMAGE_REQUEST_CODE)

            if (date.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && description.isNotEmpty()) {
                val timesheetEntry = TimesheetEntry(date, startTime, endTime, description, imageUrl = null )

                // Call a function to save the timesheet entry
                saveTimesheetEntry(timesheetEntry)

                // Clear the input fields or perform any other desired actions
                entryDateEditText.text.clear()
                startTimeEditText.text.clear()
                endTimeEditText.text.clear()
                descriptionEditText.text.clear()

                val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val isFirstLaunch = prefs.getBoolean(FIRST_LAUNCH, true)

                if (isFirstLaunch) {
                    // Clear the JSON file or perform any other first-launch actions
                    clearJsonFile()

                    // Mark that it's no longer the first launch
                    prefs.edit().putBoolean(FIRST_LAUNCH, false).apply()
                }


            }
        }
        backButton.setOnClickListener()
        {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }

    }


    private fun clearJsonFile() {
        try {
            val filename = "your_json_file_name.json"
            val outputStreamWriter = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStreamWriter.write("{}".toByteArray()) // Overwrite with an empty JSON object
            outputStreamWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun saveTimesheetEntry(entry: TimesheetEntry) {
        // Retrieve the existing list of saved entries from shared preferences
        val gson = Gson()
        val existingEntries = getSavedTimesheetEntries()

        // Add the new entry to the list
        existingEntries.add(gson.toJson(entry))

        // Convert the updated list back to a set
        val updatedEntriesSet = existingEntries.map { gson.toJson(entry) }.toSet()

        // Save the updated set of entries to shared preferences
        val sharedPreferences = getSharedPreferences("TimesheetEntries", Context.MODE_PRIVATE)
        sharedPreferences.edit().putStringSet("timesheet_entries", updatedEntriesSet).apply()
    }


    private fun getSavedTimesheetEntries(): MutableSet<String> {
        // Retrieve the list of saved entries from shared preferences
        val sharedPreferences = getSharedPreferences("TimesheetEntries", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("timesheet_entries", mutableSetOf()) ?: mutableSetOf()
    }

    private fun createCategoryTextView(categoryName: String) {
        val newCategoryTextView = TextView(this)
        newCategoryTextView.text = categoryName
        newCategoryTextView.textSize = 16f
        newCategoryTextView.setPadding(8, 8, 8, 8)

        newCategoryTextView.setOnClickListener {
            val categoryName = newCategoryTextView.text.toString()
            val categoryToRemove = categoryManager.getCategories().find { it.name == categoryName }

            if (categoryToRemove != null) {
                categoryManager.removeCategory(categoryToRemove)
                categoryContainer.removeView(newCategoryTextView)
            }
        }

        categoryContainer.addView(newCategoryTextView)
    }


    fun onImageViewClick(view: View)
    {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)

    }
}


