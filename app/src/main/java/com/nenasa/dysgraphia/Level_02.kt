package com.nenasa.dysgraphia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference

class Level_02 : AppCompatActivity() {

    var treatment: String = "false"
    var treatment_suffix: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_level02)

        val myIntent = intent
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        var word_1 = findViewById<LinearLayout>(R.id.word_1);
        var word_2 = findViewById<LinearLayout>(R.id.word_2);
        var word_3 = findViewById<LinearLayout>(R.id.word_3);
        var word_4 = findViewById<LinearLayout>(R.id.word_4);
        var word_5 = findViewById<LinearLayout>(R.id.word_5);
        var dysgraphia_l2_score = findViewById<ConstraintLayout>(R.id.dysgraphia_l2_score);

        word_1.setOnClickListener {
            draw("1");
        }
        word_2.setOnClickListener {
            draw("2");
        }
        word_3.setOnClickListener {
            draw("3");
        }
        word_4.setOnClickListener {
            draw("4");
        }
        word_5.setOnClickListener {
            draw("5");
        }
        dysgraphia_l2_score.setOnClickListener {
            showScore();
        }
    }

    fun draw(word: String) {
        val intent = Intent(this, Tracing::class.java)
        intent.putExtra("treatment", treatment)
        intent.putExtra("word", word)
        startActivity(intent)
        finish()
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra("treatment", treatment)
        startActivity(intent)
        finish()
    }

    fun showScore(){
        var sp = SharedPreference(this)
        var dysgraphia_word_1 = sp.getPreference("dysgraphia_word_1"+treatment_suffix)
        var dysgraphia_word_2 = sp.getPreference("dysgraphia_word_2"+treatment_suffix)
        var dysgraphia_word_3 = sp.getPreference("dysgraphia_word_3"+treatment_suffix)
        var dysgraphia_word_4 = sp.getPreference("dysgraphia_word_4"+treatment_suffix)
        var dysgraphia_word_5 = sp.getPreference("dysgraphia_word_5"+treatment_suffix)

        val nenasa = Nenasa()
        nenasa.showDialogBox(this, "info", "Latest Score",
            "මල : " + dysgraphia_word_1 + "%" +
                    "\nගස : " + dysgraphia_word_2 + "%" +
                    "\nගස : " + dysgraphia_word_3 + "%" +
                    "\nඉර : " + dysgraphia_word_4 + "%" +
                    "\nඅත : " + dysgraphia_word_5 + "%" +
                    "\n\nTotal Coins : " + sp.getPreference("dysgraphia_score_word"+treatment_suffix), "null", null, treatment)
    }

    override fun onBackPressed() {
    }
}