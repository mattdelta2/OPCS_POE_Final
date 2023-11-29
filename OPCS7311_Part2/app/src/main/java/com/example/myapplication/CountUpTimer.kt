package com.example.myapplication

import android.os.CountDownTimer

class CountUpTimer(initialTime: Long, interval: Long, private val onTick: (Long) -> Unit) : CountDownTimer(initialTime, interval) {

    private var elapsedMillis: Long = 0
    override fun onTick(millisUntilFinished: Long) {
        elapsedMillis += 1000
        onTick(elapsedMillis)
    }

    override fun onFinish() {
        // Timer finishes, but this should not occur in a count-up timer.
    }
}
