package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.HTTP
import com.nenasa.Services.SharedPreference
import com.nenasa.Walkthrough.Walkthrough

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val sp = SharedPreference(this)

        checkSharedPreferences(sp);

        Handler(Looper.getMainLooper()).postDelayed({
            var isNew = sp.getPreference("isNew")
            if(isNew == "false"){
                var isLoggedIn = sp.getPreference("isLoggedIn")
                    if(isLoggedIn == "true"){
                        try {
                            val http = HTTP(this, this);
                            http.request("/get_scores","{\"user_id\":\""+ sp.getPreference("user_id") +"\"}")
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
            } else {
                sp.setPreference("isNew", "false")
                val intent = Intent(this, Walkthrough::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    fun checkSharedPreferences(sp: SharedPreference){
        if(sp.getPreference("dysgraphia_letter_1") == "" || sp.getPreference("dysgraphia_letter_1").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_1") == "none")
            sp.setPreference("dysgraphia_letter_1", "0")
        if(sp.getPreference("dysgraphia_letter_2") == "" || sp.getPreference("dysgraphia_letter_2").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_2") == "none")
            sp.setPreference("dysgraphia_letter_2", "0")
        if(sp.getPreference("dysgraphia_letter_3") == "" || sp.getPreference("dysgraphia_letter_3").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_3") == "none")
            sp.setPreference("dysgraphia_letter_3", "0")
        if(sp.getPreference("dysgraphia_letter_4") == "" || sp.getPreference("dysgraphia_letter_4").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_4") == "none")
            sp.setPreference("dysgraphia_letter_4", "0")
        if(sp.getPreference("dysgraphia_letter_5") == "" || sp.getPreference("dysgraphia_letter_5").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_5") == "none")
            sp.setPreference("dysgraphia_letter_5", "0")
        if(sp.getPreference("dysgraphia_word_1") == "" || sp.getPreference("dysgraphia_word_1").isNullOrEmpty() || sp.getPreference("dysgraphia_word_1") == "none")
            sp.setPreference("dysgraphia_word_1", "0")
        if(sp.getPreference("dysgraphia_word_2") == "" || sp.getPreference("dysgraphia_word_2").isNullOrEmpty() || sp.getPreference("dysgraphia_word_2") == "none")
            sp.setPreference("dysgraphia_word_2", "0")
        if(sp.getPreference("dysgraphia_word_3") == "" || sp.getPreference("dysgraphia_word_3").isNullOrEmpty() || sp.getPreference("dysgraphia_word_3") == "none")
            sp.setPreference("dysgraphia_word_3", "0")
        if(sp.getPreference("dysgraphia_word_4") == "" || sp.getPreference("dysgraphia_word_4").isNullOrEmpty() || sp.getPreference("dysgraphia_word_4") == "none")
            sp.setPreference("dysgraphia_word_4", "0")
        if(sp.getPreference("dysgraphia_word_5") == "" || sp.getPreference("dysgraphia_word_5").isNullOrEmpty() || sp.getPreference("dysgraphia_word_5") == "none")
            sp.setPreference("dysgraphia_word_5", "0")

        if(sp.getPreference("dysgraphia_letter_1_treatment") == "" || sp.getPreference("dysgraphia_letter_1_treatment").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_1_treatment") == "none")
            sp.setPreference("dysgraphia_letter_1_treatment", "0")
        if(sp.getPreference("dysgraphia_letter_2_treatment") == "" || sp.getPreference("dysgraphia_letter_2_treatment").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_2_treatment") == "none")
            sp.setPreference("dysgraphia_letter_2_treatment", "0")
        if(sp.getPreference("dysgraphia_letter_3_treatment") == "" || sp.getPreference("dysgraphia_letter_3_treatment").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_3_treatment") == "none")
            sp.setPreference("dysgraphia_letter_3_treatment", "0")
        if(sp.getPreference("dysgraphia_letter_4_treatment") == "" || sp.getPreference("dysgraphia_letter_4_treatment").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_4_treatment") == "none")
            sp.setPreference("dysgraphia_letter_4_treatment", "0")
        if(sp.getPreference("dysgraphia_letter_5_treatment") == "" || sp.getPreference("dysgraphia_letter_5_treatment").isNullOrEmpty() || sp.getPreference("dysgraphia_letter_5_treatment") == "none")
            sp.setPreference("dysgraphia_letter_5_treatment", "0")

        if(sp.getPreference("dysgraphia_score_letter") == "" || sp.getPreference("dysgraphia_score_letter").isNullOrEmpty() || sp.getPreference("dysgraphia_score_letter") == "none")
            sp.setPreference("dysgraphia_score_letter", "0")
        if(sp.getPreference("dysgraphia_score_word") == "" || sp.getPreference("dysgraphia_score_word").isNullOrEmpty() || sp.getPreference("dysgraphia_score_word") == "none")
            sp.setPreference("dysgraphia_score_word", "0")

        if(sp.getPreference("dyscalculia_score_1") == "" || sp.getPreference("dyscalculia_score_1").isNullOrEmpty() || sp.getPreference("dyscalculia_score_1") == "none")
            sp.setPreference("dyscalculia_score_1", "0")
        if(sp.getPreference("dyscalculia_score_2") == "" || sp.getPreference("dyscalculia_score_2").isNullOrEmpty() || sp.getPreference("dyscalculia_score_2") == "none")
            sp.setPreference("dyscalculia_score_2", "0")
        if(sp.getPreference("dyscalculia_score_3") == "" || sp.getPreference("dyscalculia_score_3").isNullOrEmpty() || sp.getPreference("dyscalculia_score_3") == "none")
            sp.setPreference("dyscalculia_score_3", "0")

        if(sp.getPreference("dyscalculia_score_1_treatment") == "" || sp.getPreference("dyscalculia_score_1_treatment").isNullOrEmpty() || sp.getPreference("dyscalculia_score_1_treatment") == "none")
            sp.setPreference("dyscalculia_score_1_treatment", "0")
        if(sp.getPreference("dyscalculia_score_2_treatment") == "" || sp.getPreference("dyscalculia_score_2_treatment").isNullOrEmpty() || sp.getPreference("dyscalculia_score_2_treatment") == "none")
            sp.setPreference("dyscalculia_score_2_treatment", "0")
        if(sp.getPreference("dyscalculia_score_3_treatment") == "" || sp.getPreference("dyscalculia_score_3_treatment").isNullOrEmpty() || sp.getPreference("dyscalculia_score_3_treatment") == "none")
            sp.setPreference("dyscalculia_score_3_treatment", "0")
    }


    override fun onBackPressed() {
    }
}