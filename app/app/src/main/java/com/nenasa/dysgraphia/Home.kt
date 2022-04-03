package com.nenasa.dysgraphia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Home
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference

class Home : AppCompatActivity() {

    val nenasa = Nenasa()
    var treatment: String = "false"
    var treatment_suffix: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_home)

        val myIntent = intent
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        var level01 = findViewById<Button>(R.id.level_1);
        var level02 = findViewById<Button>(R.id.level_2);
        var reports = findViewById<Button>(R.id.dysgraphia_reports);

        var sp = SharedPreference(this)
        var dysgraphia_level_2 = sp.getPreference("dysgraphia_level_2"+treatment_suffix)
        if(dysgraphia_level_2 == "open")
            level02.isEnabled = true

        level01.setOnClickListener {
            val intent = Intent(this, Level_01::class.java)
            intent.putExtra("treatment", treatment)
            startActivity(intent)
            finish()
        }
        level02.setOnClickListener {
            val intent = Intent(this, Level_02::class.java)
            intent.putExtra("treatment", treatment)
            startActivity(intent)
            finish()
        }
        reports.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun show_score(view: View) {
        val sp = SharedPreference(this)
        var score = sp.getPreference("dysgraphia_score"+treatment_suffix)
        if(score == "null")
            score = "0";
        if(treatment == "true")
            nenasa.showDialogBox(this, "info", "Your Score", "You have earned a total of "+score+" coins in this game...", "null", null, treatment)
        else
            nenasa.showDialogBox(this, "score", "Your Score", "You have earned a total of "+score+" coins in this game...", "dysgraphia", null, treatment)
    }

    override fun onBackPressed() {
    }
}