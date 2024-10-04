package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class registrationPage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

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

            // Create user with Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        // Redirect to login page
                        startActivity(Intent(this, loginPage::class.java))
                        finish() // Close registration page
                    } else {
                        // Handle registration failure
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}