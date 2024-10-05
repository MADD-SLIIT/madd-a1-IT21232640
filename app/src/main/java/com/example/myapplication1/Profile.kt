package com.example.myapplication1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {

    private lateinit var profileUserName: EditText
    private lateinit var profileEmail: EditText
    private lateinit var profilePassword: EditText
    private lateinit var updateProfileButton: Button

    // Firebase references
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize EditText fields
        profileUserName = findViewById(R.id.profileUserName)
        profileEmail = findViewById(R.id.profileEmail)
        profilePassword = findViewById(R.id.profilePassword)  // Add profilePassword field
        updateProfileButton = findViewById(R.id.profileUpdateButton)

        // Fetch user data when activity starts
        fetchUserData()

        updateProfileButton.setOnClickListener {
            val newPassword = profilePassword.text.toString().trim()

            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)  // Call the function to update the password
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to fetch the user data from Firebase
    private fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("userName").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val password = snapshot.child("password").getValue(String::class.java)

                        // Set values to the respective EditText fields
                        profileUserName.setText(userName)
                        profileEmail.setText(email)
                        profilePassword.setText(password)  // Set password in the EditText
                    } else {
                        Toast.makeText(this@Profile, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Profile, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }



    // Function to save user data to Firebase
    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Fetching values from EditText fields
            val userName = profileUserName.text.toString().trim()
            val email = profileEmail.text.toString().trim()
            val password = profilePassword.text.toString().trim()

            // Creating a map to store the updated data
            val userMap = mapOf(
                "userName" to userName,
                "email" to email,
                "password" to password
            )

            // Write the updated data to Firebase
            database.child("users").child(userId).setValue(userMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to update the password securely using Firebase Authentication
    private fun updatePassword(newPassword: String) {
        val user = auth.currentUser

        user?.let {
            user.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Password update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
