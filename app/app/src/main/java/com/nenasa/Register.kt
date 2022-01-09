package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.HTTP

class Register : AppCompatActivity() {

    lateinit var register_fname: EditText
    lateinit var register_lname: EditText
    lateinit var register_email: EditText
    lateinit var register_phone: EditText
    lateinit var register_child_name: EditText
    lateinit var register_child_age: EditText
    lateinit var register_psw1: EditText
    lateinit var register_psw2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        register_fname = findViewById(R.id.register_fname)
        register_lname = findViewById(R.id.register_lname)
        register_email = findViewById(R.id.register_email)
        register_phone = findViewById(R.id.register_phone)
        register_child_name = findViewById(R.id.register_child_name)
        register_child_age = findViewById(R.id.register_child_age)
        register_psw1 = findViewById(R.id.register_psw1)
        register_psw2 = findViewById(R.id.register_psw2)
    }

    fun openLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun register(view: View) {
        var email = register_fname.text.toString()
        var password = register_lname.text.toString()
        try {
            val http = HTTP(this);
            http.request("register","{\"email\":\""+ email +"\", \"password\":\"" + password + "\"}")
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun showAbout(view: View) {
        val about_popup_frame_layout = findViewById<FrameLayout>(R.id.about_popup_frame_layout)
        about_popup_frame_layout.visibility = View.VISIBLE
    }

    fun hideAbout(view: View) {
        val about_popup_frame_layout = findViewById<FrameLayout>(R.id.about_popup_frame_layout)
        about_popup_frame_layout.visibility = View.GONE
    }

    override fun onBackPressed() {
    }
}