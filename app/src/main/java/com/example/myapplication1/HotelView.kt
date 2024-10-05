package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class HotelView : AppCompatActivity() {

    private lateinit var viewReviewsButton: Button
    private lateinit var bookingNowButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_view)

        // Initialize buttons
        viewReviewsButton = findViewById(R.id.viewReviewsButton)
        bookingNowButton = findViewById(R.id.bookingNowButton)

        // Set onClick listener for "View Reviews" button
        viewReviewsButton.setOnClickListener {
            val intent = Intent(this, AllFeedback::class.java)
            startActivity(intent)
        }

        // Set onClick listener for "Booking Now" button
        bookingNowButton.setOnClickListener {
            val intent = Intent(this, hotelBooking::class.java)
            startActivity(intent)
        }
    }
}
