package com.example.myapplication1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val data: List<ReviewItem>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewText: TextView = itemView.findViewById(R.id.reviewText)
        val reviewRating: TextView = itemView.findViewById(R.id.reviewRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = data[position]
        holder.reviewText.text = review.comment
        holder.reviewRating.text = review.rating.toString()
    }

    override fun getItemCount(): Int = data.size
}

// Renamed data class to avoid naming conflicts
data class ReviewItem(val comment: String, val rating: Float)
