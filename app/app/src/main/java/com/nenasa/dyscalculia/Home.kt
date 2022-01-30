package com.nenasa.dyscalculia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.nenasa.Home
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference
import com.nenasa.dysgraphia.Tracing

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyscalculia_home)

        var level_1 = findViewById<Button>(R.id.level_1);
        var level_2 = findViewById<Button>(R.id.level_2);
        var level_3 = findViewById<Button>(R.id.level_3);

        var sp = SharedPreference(this)
        var dyscalculia_level_2 = sp.getPreference("dyscalculia_level_2")
        var dyscalculia_level_3 = sp.getPreference("dyscalculia_level_3")
        if(dyscalculia_level_2 == "open")
            level_2.isEnabled = true
        if(dyscalculia_level_3 == "open")
            level_3.isEnabled = true

        level_1.setOnClickListener {
            calculate("1");
        }
        level_2.setOnClickListener {
            calculate("2");
        }
        level_3.setOnClickListener {
            calculate("3");
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun calculate(level: String) {
        val intent = Intent(this, Calculate::class.java)
        intent.putExtra("level", level)
        startActivity(intent)
        finish()
    }

    fun show_score(view: View) {
        val sp = SharedPreference(this)
        val score = sp.getPreference("dyscalculia_score")
        val nenasa = Nenasa()
        nenasa.showDialogBox(this, "info", "Your Score", "You have earned a total of "+score+" in this game...")
    }

    override fun onBackPressed() {
    }
}