package com.d3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class Walkthrough : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = WalkthroughAdapter(supportFragmentManager)
    }

    fun openLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}