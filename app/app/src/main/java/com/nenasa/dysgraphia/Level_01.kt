package com.nenasa.dysgraphia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.nenasa.R

class Level_01 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_level01)

        var letter_1 = findViewById<Button>(R.id.letter_1);
        var letter_2 = findViewById<Button>(R.id.letter_2);
        var letter_3 = findViewById<Button>(R.id.letter_3);

        letter_1.setOnClickListener {
            draw("1");
        }
        letter_2.setOnClickListener {
            draw("2");
        }
        letter_3.setOnClickListener {
            draw("3");
        }
    }

    fun draw(letter: String) {
        val intent = Intent(this, Tracing::class.java)
        intent.putExtra("letter", letter)
        startActivity(intent)
        finish()
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}