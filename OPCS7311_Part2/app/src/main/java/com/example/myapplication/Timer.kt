package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Timer : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var restartButton: ImageButton
    private lateinit var backButton: ImageButton

    private var isTimerRunning = false
    private var elapsedTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        restartButton = findViewById(R.id.restartButton)
        backButton = findViewById(R.id.backBTN)

        startButton.setOnClickListener { startTimer() }
        stopButton.setOnClickListener { stopTimer() }
        restartButton.setOnClickListener { restartTimer() }
        backButton.setOnClickListener{(back())}
    }



    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }
    private fun startTimer() {
        if (!isTimerRunning) {
            handler.post(object : Runnable {
                override fun run() {
                    elapsedTime += 1000 // Count up by 1 second
                    updateTimerText(elapsedTime)
                    handler.postDelayed(this, 1000)
                }
            })
            isTimerRunning = true
        }
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            handler.removeCallbacksAndMessages(null)
            isTimerRunning = false
        }
    }

    private fun restartTimer() {
        if (!isTimerRunning) {
            elapsedTime = 0
            updateTimerText(elapsedTime)
        }
    }

    private fun updateTimerText(timeInMillis: Long) {
        val seconds = (timeInMillis / 1000).toInt()
        val minutes = seconds / 60
        val hours = minutes / 60

        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        timerTextView.text = formattedTime
    }
}








