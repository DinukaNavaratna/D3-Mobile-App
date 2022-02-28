package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.HTTP
import android.text.TextUtils
import android.util.Patterns


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
        var fname = register_fname.text.toString()
        var lname = register_lname.text.toString()
        var email = register_email.text.toString()
        var phone = register_phone.text.toString()
        var childname = register_child_name.text.toString()
        var childage = register_child_age.text.toString()
        var password1 = register_psw1.text.toString()
        var password2 = register_psw2.text.toString()

        if(isValidEmail(email)) {
            if (password1 != password2) {
                Toast.makeText(this, "Password confirmation failed!", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val http = HTTP(this, this);
                    http.request(
                        "/register",
                        "{\"fname\":\"" + fname + "\", \"lname\":\"" + lname + "\", \"email\":\"" + email + "\", \"phone\":\"" + phone + "\", \"childname\":\"" + childname + "\", \"childage\":\"" + childage + "\", \"password\":\"" + password1 + "\"}"
                    )
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show()
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

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}