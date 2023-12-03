package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DailyGoals : AppCompatActivity() {

    private lateinit var maxGoalEditText: EditText
    private lateinit var minGoalEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_goals)

        maxGoalEditText = findViewById(R.id.editTextMaxGoal)
        minGoalEditText = findViewById(R.id.editTextMinGoal)
        saveButton = findViewById(R.id.btnSaveGoals)
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app").reference

        saveButton.setOnClickListener {
            saveDailyGoals()
        }
    }

    private fun saveDailyGoals() {
        val maxGoalText = maxGoalEditText.text.toString()
        val minGoalText = minGoalEditText.text.toString()

        if (maxGoalText.isNotBlank() && minGoalText.isNotBlank()) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            // Convert input to Float (you may want to add proper error handling here)
            val maxGoal = maxGoalText.toFloat()
            val minGoal = minGoalText.toFloat()

            val dailyGoals = Goals(maxGoal, minGoal)

            // Store goals in the database
            if (userId != null) {
                databaseReference.child("daily_goals").child(userId).setValue(dailyGoals)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@DailyGoals,
                                "Goals saved successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to the Graphs activity
                            val intent = Intent(this@DailyGoals, Graphs::class.java)
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
            }
        } else {
            Toast.makeText(
                this@DailyGoals,
                "Please enter both maximum and minimum goals",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}