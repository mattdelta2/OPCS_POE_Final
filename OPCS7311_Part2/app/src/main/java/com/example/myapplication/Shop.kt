package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Shop : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        backButton = findViewById(R.id.backBTN)


        backButton.setOnClickListener{(back())}



    }


    private fun back()
    {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }



}