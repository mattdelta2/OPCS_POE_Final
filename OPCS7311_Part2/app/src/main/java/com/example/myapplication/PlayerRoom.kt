package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class PlayerRoom : AppCompatActivity() {
    private lateinit var backButton: ImageButton

    private lateinit var shopButton: ImageButton

    private lateinit var timerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_room)

        backButton = findViewById(R.id.backBtn)

        shopButton = findViewById((R.id.Shopbtn))

        timerButton = findViewById(R.id.Timerbtn)

        backButton.setOnClickListener{(back())}

        shopButton.setOnClickListener{(shop())}

        timerButton.setOnClickListener{(timer())}


    }

    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }

    private fun shop()
    {
        val intent = Intent(this, Shop::class.java)
        startActivity(intent)
        finish()
    }

    private fun timer()
    {
        val intent = Intent(this, Shop::class.java)
        startActivity(intent)
        finish()
    }
}