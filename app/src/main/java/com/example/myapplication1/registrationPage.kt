package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class registrationPage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val TAG = "RegistrationPage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_page)

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Find UI elements
        val usernameEditText = findViewById<EditText>(R.id.regUserName)
        val emailEditText = findViewById<EditText>(R.id.regEmail)
        val passwordEditText = findViewById<EditText>(R.id.regPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.regConfirmPassword)
        val regButton = findViewById<Button>(R.id.regButton)

        // Register button click listener
        regButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Basic validation
            if (usernameEditText.text.isEmpty()) {
                usernameEditText.error = "Username required"
                usernameEditText.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Email required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordEditText.error = "Confirm password required"
                confirmPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordEditText.error = "Passwords do not match"
                confirmPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            // Log email and password (make sure not to log sensitive data in production)
            Log.d(TAG, "Attempting to register with Email: $email")

            // Create user with Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid
                        val username = usernameEditText.text.toString().trim()

                        // Save user details in the database
                        val userMap = mapOf(
                            "userName" to username,
                            "email" to email,
                            "password" to password // Avoid saving passwords in plain text
                        )

                        userId?.let {
                            database.child("users").child(it).setValue(userMap)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Log.d(TAG, "User details saved for $email")
                                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                        // Redirect to login page
                                        startActivity(Intent(this, loginPage::class.java))
                                        finish() // Close registration page
                                    } else {
                                        Log.e(TAG, "Failed to save user details: ${dbTask.exception?.message}")
                                        Toast.makeText(this, "Failed to save user details", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        // Handle registration failure
                        Log.e(TAG, "Registration failed: ${task.exception?.message}")
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
