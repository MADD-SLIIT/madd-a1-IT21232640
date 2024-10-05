package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R
import com.google.firebase.firestore.FirebaseFirestore

class ReviewConfirmation : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private var reviewId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_confirmation)

        firestore = FirebaseFirestore.getInstance()

        // Retrieve data from intent
        reviewId = intent.getStringExtra("reviewId")
        val reviewText = intent.getStringExtra("reviewText")
        val ratingValue = intent.getFloatExtra("ratingValue", 0f)

        // Update TextView
        val confirmationTextView: TextView = findViewById(R.id.confirmationTextView)
        confirmationTextView.text = "Your review: $reviewText\nRating: $ratingValue"

        val updateButton: Button = findViewById(R.id.comUpbtn1)
        updateButton.setOnClickListener {
            // Navigate to ReviewPage with existing review ID
            val intent = Intent(this, ReviewPage::class.java)
            intent.putExtra("reviewId", reviewId)
            startActivity(intent)
        }

        val deleteButton: Button = findViewById(R.id.comDelebtn2)
        deleteButton.setOnClickListener {
            reviewId?.let {
                firestore.collection("reviews").document(it).delete()
                    .addOnSuccessListener {
                        // Handle successful deletion
                        finish() // Close this activity
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace() // Handle error
                    }
            }
        }
    }
}
