package com.d3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun openLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun register(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun showAbout(view: View) {
        val about_popup_frame_layout = findViewById<FrameLayout>(R.id.about_popup_frame_layout)
        about_popup_frame_layout.visibility = View.VISIBLE
    }

    fun hideAbout(view: View) {
        val about_popup_frame_layout = findViewById<FrameLayout>(R.id.about_popup_frame_layout)
        about_popup_frame_layout.visibility = View.GONE
    }
}