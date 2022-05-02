package com.nenasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.nenasa.Services.HTTP
import com.nenasa.Services.SharedPreference
import com.nenasa.dyslexia.testUpload

class Hidden : AppCompatActivity() {

    lateinit var ServerHost: EditText
    lateinit var sp: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hidden)

        ServerHost = findViewById(R.id.serverhost)
        sp = SharedPreference(this)
        sp.getPreference("ServerHost")
        ServerHost.setText((sp.getPreference("ServerHost")).toString())
    }

    fun UpdateServerHost(view: View){
        val serverhost = ServerHost.text
        sp.setPreference("ServerHost", serverhost.toString())
        val intent = Intent(this, Splash::class.java)
        startActivity(intent)
        finish()
    }

    fun ServerHealth(view: View){
        try {
            val http = HTTP(this, this);
            http.request("/","{}")
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun ClearSharedPreferences(view: View){
        sp.clearPreference()
        val intent = Intent(this, Splash::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this, Splash::class.java)
        startActivity(intent)
        finish()
    }
}