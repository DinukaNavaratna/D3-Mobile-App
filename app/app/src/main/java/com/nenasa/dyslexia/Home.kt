package com.nenasa.dyslexia

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import com.nenasa.Home
import com.nenasa.R
import android.widget.Toast

import android.widget.RadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nenasa.Nenasa
import com.nenasa.Services.SharedPreference
import com.nenasa.dysgraphia.Level_01
import java.io.IOException

class Home : AppCompatActivity() {

    lateinit var dyslecia_group1: RadioGroup
    lateinit var dyslecia_group2: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_home)

        /*
        dyslecia_group1 = findViewById<RadioGroup>(R.id.dyslecia_group1);
        dyslecia_group2 = findViewById<RadioGroup>(R.id.dyslecia_group2);

        dyslecia_group1.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {group, checkedId -> // checkedId is the RadioButton selected
            if(dyslecia_group2.getCheckedRadioButtonId() != -1){
                dyslecia_group2.clearCheck();
            }
        })
        dyslecia_group2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {group, checkedId -> // checkedId is the RadioButton selected
            if(dyslecia_group1.getCheckedRadioButtonId() != -1){
                dyslecia_group1.clearCheck();
            }
        })
        */

        val easy: Button = findViewById<Button>(R.id.easy);
        val hard: Button = findViewById<Button>(R.id.hard);

        easy.setOnClickListener {
            val intent = Intent(this, HomeEasy::class.java)
            startActivity(intent)
            finish()
        }
        hard.setOnClickListener {
            val intent = Intent(this, HomeHard::class.java)
            startActivity(intent)
            finish()
        }
    }
/*
    fun start(view: View) {
        if(dyslecia_group1.getCheckedRadioButtonId() == -1 && dyslecia_group2.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select an option to proceed!", Toast.LENGTH_SHORT).show()
        } else {
            var dyslecia_group_id = 0
            if(dyslecia_group1.getCheckedRadioButtonId() != -1) {
                dyslecia_group_id  = dyslecia_group1.checkedRadioButtonId
            } else if(dyslecia_group2.getCheckedRadioButtonId() != -1) {
                dyslecia_group_id  = dyslecia_group2.checkedRadioButtonId
            }
            val selectedItem = findViewById<RadioButton>(dyslecia_group_id)

            if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                val intent = Intent(this, Read::class.java)
                intent.putExtra("level", selectedItem.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
*/
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