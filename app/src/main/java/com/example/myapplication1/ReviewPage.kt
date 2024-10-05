package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication1.R
import com.google.firebase.firestore.FirebaseFirestore

class ReviewPage : AppCompatActivity() {
    private lateinit var reviewEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_review_page)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        reviewEditText = findViewById(R.id.editTextTextMultiLine2)
        ratingBar = findViewById(R.id.ratingBar)

        val submitButton: Button = findViewById(R.id.button5)
        submitButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            val ratingValue = ratingBar.rating

            // Save to Firestore
            val reviewData = hashMapOf(
                "reviewText" to reviewText,
                "ratingValue" to ratingValue
            )
            firestore.collection("reviews").add(reviewData)
                .addOnSuccessListener { documentReference ->
                    val intent = Intent(this, ReviewConfirmation::class.java)
                    intent.putExtra("reviewId", documentReference.id) // Pass the review ID
                    intent.putExtra("reviewText", reviewText)
                    intent.putExtra("ratingValue", ratingValue)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    // Handle error
                    e.printStackTrace()
                }
        }
    }
}
