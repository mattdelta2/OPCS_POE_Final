package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DailyGoals : AppCompatActivity() {

    private lateinit var maxGoalEditText: EditText
    private lateinit var minGoalEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_goals)

        maxGoalEditText = findViewById(R.id.editTextMaxGoal)
        minGoalEditText = findViewById(R.id.editTextMinGoal)
        dateEditText = findViewById(R.id.editTextDate)
        saveButton = findViewById(R.id.btnSaveGoals)
        backButton = findViewById(R.id.btnBack)
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app").reference

        saveButton.setOnClickListener {
            saveDailyGoals()
        }
        backButton.setOnClickListener()
        {
            back()
        }
    }

    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveDailyGoals() {
        val maxGoalText = maxGoalEditText.text.toString()
        val minGoalText = minGoalEditText.text.toString()
        val date = dateEditText.text.toString()

        if (maxGoalText.isNotBlank() && minGoalText.isNotBlank() && date.isNotBlank()) {
            // Convert input to String
            val maxGoal = maxGoalText
            val minGoal = minGoalText

            val dailyGoals = Goals(maxGoal, minGoal, date)

            // Store goals in the database
            databaseReference.child("daily_goals").setValue(dailyGoals)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@DailyGoals,
                            "Goals saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@DailyGoals, MainMenu::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@DailyGoals,
                            "Failed to save goals. ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this@DailyGoals,
                "Please enter all fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
