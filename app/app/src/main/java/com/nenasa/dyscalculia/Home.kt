package com.nenasa.dyscalculia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.nenasa.Home
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference
import com.nenasa.dysgraphia.Tracing

class Home : AppCompatActivity() {

    val nenasa = Nenasa()
    var treatment: String = "false"
    var treatment_suffix: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyscalculia_home)

        val myIntent = intent
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        Toast.makeText(this, "Dyscalculia"+treatment_suffix, Toast.LENGTH_SHORT).show()

        var level_1 = findViewById<Button>(R.id.level_1);
        var level_2 = findViewById<Button>(R.id.level_2);
        var level_3 = findViewById<Button>(R.id.level_3);
        var reports = findViewById<Button>(R.id.dyscalculia_reports);

        var sp = SharedPreference(this)
        var dyscalculia_level_2 = sp.getPreference("dyscalculia_level_2"+treatment_suffix)
        var dyscalculia_level_3 = sp.getPreference("dyscalculia_level_3"+treatment_suffix)
        if(dyscalculia_level_2 == "open")
            level_2.isEnabled = true
        if(dyscalculia_level_3 == "open")
            level_3.isEnabled = true

        level_1.setOnClickListener {
            calculate("1", treatment);
        }
        level_2.setOnClickListener {
            calculate("2", treatment);
        }
        level_3.setOnClickListener {
            calculate("3", treatment);
        }
        reports.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            intent.putExtra("treatment", treatment)
            startActivity(intent)
            finish()
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun calculate(level: String, treatment: String) {
        val intent = Intent(this, Calculate::class.java)
        intent.putExtra("treatment", treatment)
        intent.putExtra("level", level)
        startActivity(intent)
        finish()
    }

    fun show_score(view: View) {
        val sp = SharedPreference(this)
        var score1 = sp.getPreference("dyscalculia_score_1"+treatment_suffix)
        var score2 = sp.getPreference("dyscalculia_score_2"+treatment_suffix)
        var score3 = sp.getPreference("dyscalculia_score_3"+treatment_suffix)
        var score = score1.toInt() + score2.toInt() + score3.toInt()
        if(treatment == "true")
            nenasa.showDialogBox(this, "info", "Your Score", "Level 01: "+score1+"\nLevel 02: "+score2+"\nLevel 03: "+score3+"\n\nTotal Coins: "+score, "null", null, treatment)
        else
            nenasa.showDialogBox(this, "score", "Your Score", "Level 01: "+score1+"\nLevel 02: "+score2+"\nLevel 03: "+score3+"\n\nTotal Coins: "+score, "dyscalculia", null, treatment)
    }

    override fun onBackPressed() {
    }
}