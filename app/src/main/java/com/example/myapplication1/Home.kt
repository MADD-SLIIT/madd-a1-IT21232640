package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import androidx.cardview.widget.CardView // Import for CardView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the click listener for the homeNavProfile button
        val profileButton: ImageButton = findViewById(R.id.homeNavProfile)
        profileButton.setOnClickListener {
            // Start the Profile activity
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        // Set up the click listener for the cardViewNearLocation1
        val cardViewNearLocation1: CardView = findViewById(R.id.cardViewNearLocation1)
        cardViewNearLocation1.setOnClickListener {
            // Start the HotelView activity
            val intent = Intent(this, HotelView::class.java)
            startActivity(intent)
        }
    }
}