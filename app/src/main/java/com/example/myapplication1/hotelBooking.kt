package com.example.myapplication1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class hotelBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hotel_booking)

        // Firebase Firestore initialization
        val db = FirebaseFirestore.getInstance()

        // Initialize EditText views
        val bookingDate = findViewById<EditText>(R.id.bookingDate)
        val bookingTime = findViewById<EditText>(R.id.bookingTime)
        val bookingName = findViewById<EditText>(R.id.bookingName)
        val bookingPhoneNumber = findViewById<EditText>(R.id.bookingPhoneNumber)
        val bookingNumPerson = findViewById<EditText>(R.id.bookingNumPerson)
        val bookButton = findViewById<Button>(R.id.bookButton)

        // Handle the bookingDate EditText click to show DatePickerDialog
        bookingDate.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    bookingDate.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        // Handle the bookingTime EditText click to show TimePickerDialog
        bookingTime.setOnClickListener {
            // Get the current time
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Show the TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    bookingTime.setText(formattedTime)
                },
                hour, minute, true // Use 24-hour format
            )
            timePickerDialog.show()
        }

        // Set OnClickListener for the bookButton to store data in Firestore
        bookButton.setOnClickListener {
            val name = bookingName.text.toString()
            val phoneNumber = bookingPhoneNumber.text.toString()
            val numPersons = bookingNumPerson.text.toString()
            val date = bookingDate.text.toString()
            val time = bookingTime.text.toString()

            if (name.isNotEmpty() && phoneNumber.isNotEmpty() && numPersons.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                // Create a booking data map to store in Firestore
                val bookingData = hashMapOf(
                    "name" to name,
                    "phoneNumber" to phoneNumber,
                    "numPersons" to numPersons,
                    "date" to date,
                    "time" to time
                )

                // Store the data in Firestore under the "bookings" collection
                db.collection("bookings")
                    .add(bookingData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Booking successfully added!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding booking: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
