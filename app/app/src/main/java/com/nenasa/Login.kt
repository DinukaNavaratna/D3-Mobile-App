package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.HTTP
import org.json.JSONObject

class Login : AppCompatActivity() {

    lateinit var login_email: EditText
    lateinit var login_psw: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        login_email = findViewById(R.id.login_email)
        login_psw = findViewById(R.id.login_psw)
    }

    fun openRegister(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    fun login(view: View) {
        var email = login_email.text.toString()
        var password = login_psw.text.toString()
        try {
            val http = HTTP(this);
            http.request("login","{\"email\":\""+ email +"\", \"password\":\"" + password + "\"}")
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
}