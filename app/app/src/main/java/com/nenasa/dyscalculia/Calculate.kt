package com.nenasa.dyscalculia

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.HTTP
import com.nenasa.Services.SharedPreference
import kotlin.random.Random

class Calculate : AppCompatActivity() {

    var startNumber = 0;
    var endNumber = 0;
    val sign1 = arrayOf<String>("+", "-")
    val sign2 = arrayOf<String>("+", "-", "x", "÷")
    lateinit var sign_arr: String;
    var correct_answer = 0;
    var questionCount = 1;
    var correctCount = 0;
    lateinit var finishButton: Button;
    private lateinit var level: String;
    private lateinit var level_name: String;
    var time_spent: Long = 0
    val nenasa = Nenasa();
    lateinit var treatment: String;
    var treatment_suffix: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyscalculia_calculate)

        val myIntent = intent
        level = myIntent.getStringExtra("level").toString()
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        finishButton = findViewById<Button>(R.id.finish_btn)

        if(level.equals("1")){
            startNumber = 0;
            endNumber = 5;
            sign_arr = "sign1"
            level_name = "Easy"
        } else if(level.equals("2")){
            startNumber = 0;
            endNumber = 9;
            sign_arr = "sign1"
            level_name = "Medium"
        } else if(level.equals("3")){
            startNumber = 0;
            endNumber = 20;
            sign_arr = "sign2"
            level_name = "Hard"
        }
        newCalculation();

        val maxCounter: Long
        maxCounter = if (treatment === "true") {
            60000
        } else {
            600000
        }
        val diff: Long = 1000
        object : CountDownTimer(maxCounter, diff) {
            override fun onTick(millisUntilFinished: Long) {
                val diff = maxCounter - millisUntilFinished
                time_spent++
                if (diff <= 10) {
                    if (myIntent.getStringExtra("treatment") === "true") {
                        Toast.makeText(
                            applicationContext,
                            "Time left: $diff seconds",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFinish() {
                time_spent++
                nenasa.showDialogBox(this@Calculate,"error","Opz!","You ran out of time. Try again!", "null", null, treatment)
            }
        }.start()

        var answer = findViewById<EditText>(R.id.answer);
        answer.setOnEditorActionListener(OnEditorActionListener {v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer()
            }
            false
        })
    }

    fun newCalculation() {
        var number1 = getRandomNumber();
        var number2 = getRandomNumber();
        var sign = "";
        if(sign_arr.equals("sign1")){
            sign = sign1.random()
        } else {
            sign = sign2.random()
            if(sign.equals("÷")){
                number1 = number1*number2;
            }
        }
        if(sign.equals("-")){
            if(number1 < number2) {
                val number0 = number1;
                number1 = number2;
                number2 = number0;
            }
            correct_answer = number1 - number2;
        } else if(sign.equals("+")){
            correct_answer = number1 + number2;
        } else if(sign.equals("x")){
            correct_answer = number1 * number2;
        } else if(sign.equals("÷")){
            correct_answer = number1 / number2;
        }

        questionCount++;

        val number1_txt = findViewById<TextView>(R.id.number_1)
        val number2_txt = findViewById<TextView>(R.id.number_2)
        val sign_txt = findViewById<TextView>(R.id.sign)

        number1_txt.text = number1.toString()
        number2_txt.text = number2.toString()
        sign_txt.text = sign

        if (questionCount > 5) {
            finishButton.isEnabled = true
        }
    }

    fun getRandomNumber(): Int {
        require(startNumber <= endNumber) { "Illegal Argument" }
        val rand = Random(System.nanoTime())
        return (startNumber..endNumber).random(rand)
    }

    fun checkAnswer(view: View){
        checkAnswer()
    }

    fun checkAnswer(){
        val answerBox = findViewById<EditText>(R.id.answer)
        val answer = answerBox.text.toString()
        answerBox.text.clear()

        if(answer.trim().length != 0){
            if (correct_answer == answer.toInt()){
                correctCount++;
            }
        }
        if (questionCount <= 5){
            newCalculation();
        } else {
            var coins: Int = 0;
            var sp = SharedPreference(this)
            var user_id = sp.getPreference("user_id")

            if(level.equals("1")) {
                coins = correctCount;
            } else if(level.equals("2")) {
                coins = correctCount*2;
            } else if(level.equals("3")) {
                coins = correctCount * 3;
            }

            try {
                val score: Int = sp.getPreference("dyscalculia_score_"+level).toInt() + coins
                sp.setPreference("dyscalculia_score_"+level+treatment_suffix, Integer.toString(score))
                val http = HTTP(this, this)
                /*http.request(
                    "/update_scores",
                    "{\"userid\":\"" + user_id + "\", \"game\":\"dyscalculia\", \"score\":\"" + score + "\"}"
                )*/
                http.request(
                    "/insert_scores",
                    "{\"user_id\":\"" + user_id + "\", \"game\":\"dyscalculia"+treatment_suffix+"\", \"score\":\""+ coins +"\", \"query\":\"INSERT INTO dyscalculia_score (user_id, level, correct, wrong, duration, accuracy, points) VALUES ('"
                            +user_id+"','"+level_name+treatment_suffix+"', "+correctCount+","+(5-correctCount)+","+time_spent+",'"+((correctCount/5)*100)+"',"+coins+")\"}"
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            if(level.equals("1")) {
                if(correctCount>=4){
                    sp.setPreference("dyscalculia_level_2"+treatment_suffix, "open")
                    nenasa.showDialogBox(this, "info", "Congratulations!", "Level 02 Unlocked!", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                } else {
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
                    nenasa.showDialogBox(this, "info", "Try again!", correctCount.toString() + " correct out of 5", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                }
            } else if(level.equals("2")) {
                if(correctCount>=4){
                    sp.setPreference("dyscalculia_level_3"+treatment_suffix, "open")
                    Toast.makeText(applicationContext, "Congratulations!\nLevel 03 Unlocked!",Toast.LENGTH_SHORT).show()
                    nenasa.showDialogBox(this, "info", "Congratulations!", "Level 03 Unlocked!", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                } else {
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
                    nenasa.showDialogBox(this, "info", "Try again!", correctCount.toString() + " correct out of 5", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                }
            } else if(level.equals("3")) {
                if(correctCount>=4){
                    Toast.makeText(applicationContext, "Congratulations!\nYou Passed this Level!",Toast.LENGTH_SHORT).show()
                    nenasa.showDialogBox(this, "info", "Congratulations!", "You Passed this Level!", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                } else {
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
                    nenasa.showDialogBox(this, "info", "Try again!", correctCount.toString() + " correct out of 5", "redirect", Intent(this, com.nenasa.dyscalculia.Home::class.java), treatment)
                }
            }
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, com.nenasa.dyscalculia.Home::class.java)
        intent.putExtra("treatment", treatment)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}