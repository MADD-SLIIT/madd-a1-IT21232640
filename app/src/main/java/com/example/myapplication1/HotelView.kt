package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class HotelView : AppCompatActivity() {

    private lateinit var viewReviewsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_view)

        viewReviewsButton = findViewById(R.id.viewReviewsButton)

        viewReviewsButton.setOnClickListener {
            val intent = Intent(this, AllFeedback::class.java)
            startActivity(intent)
        }
    }
}
