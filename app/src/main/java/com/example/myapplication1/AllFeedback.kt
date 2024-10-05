package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class AllFeedback : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_feedback)

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFeedback)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviewList)
        recyclerView.adapter = reviewAdapter

        // Fetch the reviews from Firestore
        fetchReviews()

        // Add click listener to the "Add Comment" button
        val addCommentButton: Button = findViewById(R.id.addCommentButton)
        addCommentButton.setOnClickListener {
            // Start the ReviewPage activity
            val intent = Intent(this@AllFeedback, ReviewPage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchReviews() {
        firestore.collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                reviewList.clear()  // Clear the list to avoid duplicates
                for (document: QueryDocumentSnapshot in result) {
                    val review = document.toObject(Review::class.java)
                    reviewList.add(review)
                }
                reviewAdapter.notifyDataSetChanged()  // Notify adapter of data changes
            }
            .addOnFailureListener { e ->
                // Handle the error
                e.printStackTrace()
            }
    }
}
