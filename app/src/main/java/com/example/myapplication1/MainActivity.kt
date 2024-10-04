
package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle registration button click
        val registrationButton = findViewById<Button>(R.id.registrationButton)
        registrationButton.setOnClickListener {
            // Start the registrationPage activity
            val intent = Intent(this, registrationPage::class.java)
            startActivity(intent)
        }

        // Handle login button click
        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            // Start the loginPage activity
            val intent = Intent(this, loginPage::class.java)
            startActivity(intent)
        }
    }
}
