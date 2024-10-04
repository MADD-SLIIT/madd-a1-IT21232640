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
    private lateinit var profilePhoneNumber: EditText
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
        updateProfileButton = findViewById(R.id.profileUpdateButton)

        // Fetch user data
        fetchUserData()

        updateProfileButton.setOnClickListener {
            updateUserData()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("userName").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)

                        profileUserName.setText(userName)
                        profileEmail.setText(email)
                        profilePhoneNumber.setText(phoneNumber)
                    } else {
                        Toast.makeText(this@Profile, "User data does not exist", Toast.LENGTH_SHORT).show()
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

    private fun updateUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userName = profileUserName.text.toString().trim()
            val email = profileEmail.text.toString().trim()
            val phoneNumber = profilePhoneNumber.text.toString().trim()

            // Update user data in the database
            val userMap = mapOf("userName" to userName, "email" to email, "phoneNumber" to phoneNumber)
            database.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
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
}
