package com.nenasa

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.HTTP
import com.nenasa.Services.SharedPreference

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        var username: TextView = findViewById(R.id.username)
        val sp = SharedPreference(this)
        username.setText(sp.getPreference("username"))
    }
}