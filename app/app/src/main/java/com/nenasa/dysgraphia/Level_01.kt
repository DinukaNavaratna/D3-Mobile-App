package com.nenasa.dysgraphia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference

class Level_01 : AppCompatActivity() {

    lateinit var dysgraphia_letter_1: String;
    lateinit var dysgraphia_letter_2: String;
    lateinit var dysgraphia_letter_3: String;
    lateinit var dysgraphia_letter_4: String;
    lateinit var dysgraphia_letter_5: String;
    lateinit var sp: SharedPreference;
    var treatment: String = "false"
    var treatment_suffix: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_level01)

        val myIntent = intent
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        sp = SharedPreference(this)
        dysgraphia_letter_1 = sp.getPreference("dysgraphia_letter_1"+treatment_suffix)
        dysgraphia_letter_2 = sp.getPreference("dysgraphia_letter_2"+treatment_suffix)
        dysgraphia_letter_3 = sp.getPreference("dysgraphia_letter_3"+treatment_suffix)
        dysgraphia_letter_4 = sp.getPreference("dysgraphia_letter_4"+treatment_suffix)
        dysgraphia_letter_5 = sp.getPreference("dysgraphia_letter_5"+treatment_suffix)

        if(dysgraphia_letter_1.toFloat() > 50 && dysgraphia_letter_2.toFloat() > 50 && dysgraphia_letter_3.toFloat() > 50 && dysgraphia_letter_4.toFloat() > 50 && dysgraphia_letter_5.toFloat() > 50){
            sp.setPreference("dysgraphia_level_2"+treatment_suffix, "open")
        }

        var letter_1 = findViewById<LinearLayout>(R.id.letter_1);
        var letter_2 = findViewById<LinearLayout>(R.id.letter_2);
        var letter_3 = findViewById<LinearLayout>(R.id.letter_3);
        var letter_4 = findViewById<LinearLayout>(R.id.letter_4);
        var letter_5 = findViewById<LinearLayout>(R.id.letter_5);
        var dysgraphia_l1_score = findViewById<ConstraintLayout>(R.id.dysgraphia_l1_score);

        letter_1.setOnClickListener {
            draw("1");
        }
        letter_2.setOnClickListener {
            draw("2");
        }
        letter_3.setOnClickListener {
            draw("3");
        }
        letter_4.setOnClickListener {
            draw("4");
        }
        letter_5.setOnClickListener {
            draw("5");
        }
        dysgraphia_l1_score.setOnClickListener {
            showScore();
        }
    }

    fun draw(letter: String) {
        val intent = Intent(this, Tracing::class.java)
        intent.putExtra("treatment", treatment)
        intent.putExtra("letter", letter)
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
        val nenasa = Nenasa()
        nenasa.showDialogBox(this, "info", "Latest Score",
            "ක : " + dysgraphia_letter_1 + "%" +
                    "\nර : " + dysgraphia_letter_2 + "%" +
                    "\nල : " + dysgraphia_letter_3 + "%" +
                    "\nට : " + dysgraphia_letter_4 + "%" +
                    "\nඉ : " + dysgraphia_letter_5 + "%" +
                    "\n\nTotal Coins : " + sp.getPreference("dysgraphia_score_letter"+treatment_suffix), "null", null, treatment)
    }

    override fun onBackPressed() {
    }
}