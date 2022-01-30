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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_home)

        var level01 = findViewById<Button>(R.id.level_1);
        var level02 = findViewById<Button>(R.id.level_2);
        var reports = findViewById<Button>(R.id.reports);

        level01.setOnClickListener {
            val intent = Intent(this, Level_01::class.java)
            startActivity(intent)
            finish()
        }
        level02.setOnClickListener {
            val intent = Intent(this, Level_02::class.java)
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
        val score = sp.getPreference("dysgraphia_score")
        val nenasa = Nenasa()
        nenasa.showDialogBox(this, "info", "Your Score", "You have earned a total of "+score+" in this game...")
    }

    override fun onBackPressed() {
    }
}