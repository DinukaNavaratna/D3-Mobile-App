package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.SharedPreference
import com.nenasa.Walkthrough.Walkthrough

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val sp = SharedPreference(this)

        Handler(Looper.getMainLooper()).postDelayed({
            var isNew = sp.getPreference("isNew")
            if(isNew == "false"){
                var isLoggedIn = sp.getPreference("isLoggedIn")
                    if(isLoggedIn == "true"){
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
            } else {
                sp.setPreference("isNew", "false")
                val intent = Intent(this, Walkthrough::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    override fun onBackPressed() {
    }
}