package com.nenasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import android.widget.RadioButton
import android.widget.RadioGroup
import com.nenasa.Services.SharedPreference


class Questions : AppCompatActivity() {

    lateinit var radioGroup_01: RadioGroup
    lateinit var radioGroup_02: RadioGroup
    lateinit var radioGroup_03: RadioGroup
    lateinit var radioGroup_04: RadioGroup
    var selected: String = "none";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questions)

        radioGroup_01 = findViewById<RadioGroup>(R.id.radioGroup_01);
        radioGroup_02 = findViewById<RadioGroup>(R.id.radioGroup_02);
        radioGroup_03 = findViewById<RadioGroup>(R.id.radioGroup_03);
        radioGroup_04 = findViewById<RadioGroup>(R.id.radioGroup_04);

        val radio_yes_1 = findViewById<RadioButton>(R.id.yes_01);
        val radio_yes_2 = findViewById<RadioButton>(R.id.yes_02);
        val radio_yes_3 = findViewById<RadioButton>(R.id.yes_03);
        val radio_yes_4 = findViewById<RadioButton>(R.id.yes_04);

        radio_yes_1.setOnClickListener {
            selected = "dyslexia";
            findViewById<RadioButton>(R.id.no_02).isChecked = true
            findViewById<RadioButton>(R.id.no_03).isChecked = true
            findViewById<RadioButton>(R.id.no_04).isChecked = true
        }
        radio_yes_2.setOnClickListener {
            selected = "dysgraphia";
            findViewById<RadioButton>(R.id.no_01).isChecked = true
            findViewById<RadioButton>(R.id.no_03).isChecked = true
            findViewById<RadioButton>(R.id.no_04).isChecked = true
        }
        radio_yes_3.setOnClickListener {
            selected = "dyscalculia";
            findViewById<RadioButton>(R.id.no_02).isChecked = true
            findViewById<RadioButton>(R.id.no_01).isChecked = true
            findViewById<RadioButton>(R.id.no_04).isChecked = true
        }
        radio_yes_4.setOnClickListener {
            selected = "dyslexia";
            findViewById<RadioButton>(R.id.no_02).isChecked = true
            findViewById<RadioButton>(R.id.no_03).isChecked = true
            findViewById<RadioButton>(R.id.no_01).isChecked = true
        }
    }

    fun openHome(view: View) {
        openHome()
    }

    fun submitQuestions(view: View){
        val sp = SharedPreference(this)
        sp.setPreference("selection", selected)
        openHome()
    }

    fun openHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}