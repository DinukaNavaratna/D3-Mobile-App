package com.nenasa.Walkthrough

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.nenasa.Login
import com.nenasa.R

class Walkthrough : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walkthrough)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = WalkthroughAdapter(supportFragmentManager)
    }

    fun openLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}