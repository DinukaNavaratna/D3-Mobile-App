package com.nenasa.dyslexia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nenasa.R

class HomeHard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_homehard)
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}