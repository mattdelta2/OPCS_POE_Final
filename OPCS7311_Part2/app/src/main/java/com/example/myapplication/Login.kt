package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://opcs-poe-final-default-rtdb.europe-west1.firebasedatabase.app").reference

        val loginButton = findViewById<ImageButton>(R.id.btnLogin)
        val registerButton = findViewById<ImageButton>(R.id.btnRegister)

        loginButton.setOnClickListener {
            handleLogin()
        }

        registerButton.setOnClickListener {
            handleRegistration()
        }
    }

    private fun handleLogin() {
        // Get email and password from user input
        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User login successful, navigate to MainMenu
                    val intent = Intent(this, MainMenu::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If login fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleRegistration() {
        // Get email and password from user input
        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Example: Register user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    Toast.makeText(baseContext, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}

