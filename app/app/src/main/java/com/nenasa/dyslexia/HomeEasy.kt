package com.nenasa.dyslexia

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference

class HomeEasy : AppCompatActivity() {

    lateinit var dyslecia_group1: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_homeeasy)

        dyslecia_group1 = findViewById<RadioGroup>(R.id.dyslecia_group1);
    }

    fun start(view: View) {
        if(dyslecia_group1.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select an option to proceed!", Toast.LENGTH_SHORT).show()
        } else {
            var dyslecia_group_id = 0
            if(dyslecia_group1.getCheckedRadioButtonId() != -1) {
                dyslecia_group_id  = dyslecia_group1.checkedRadioButtonId
            }
            val selectedItem = findViewById<RadioButton>(dyslecia_group_id)

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                val intent = Intent(this, Read::class.java)
                intent.putExtra("level", "Easy"+selectedItem.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun show_score(view: View) {
        val sp = SharedPreference(this)
        var score = sp.getPreference("dyslexia_score")
        if(score == "null")
            score = "0";
        val nenasa = Nenasa()
        nenasa.showDialogBox(this, "info", "Your Score", "You have earned a total of "+score+" coins in this game...")
    }

    override fun onBackPressed() {
    }
}