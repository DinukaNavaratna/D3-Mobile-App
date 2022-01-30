package com.nenasa.dyscalculia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.nenasa.R
import com.nenasa.Services.SharedPreference
import com.nenasa.dysgraphia.Home
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyscalculia_calculate)

        val myIntent = intent
        level = myIntent.getStringExtra("level").toString()

        finishButton = findViewById<Button>(R.id.finish_btn)

        if(level.equals("1")){
            startNumber = 0;
            endNumber = 5;
            sign_arr = "sign1"
        } else if(level.equals("2")){
            startNumber = 0;
            endNumber = 9;
            sign_arr = "sign1"
        } else if(level.equals("3")){
            startNumber = 0;
            endNumber = 20;
            sign_arr = "sign2"
        }
        newCalculation();
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
            var sp = SharedPreference(this)
            if(level.equals("1")) {
                if(correctCount>=4){
                    sp.setPreference("dyscalculia_level_2", "open")
                    Toast.makeText(applicationContext, "Congradulations!\nLevel 02 Unlocked!",Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
            } else if(level.equals("2")) {
                if(correctCount>=4){
                    sp.setPreference("dyscalculia_level_3", "open")
                    Toast.makeText(applicationContext, "Congradulations!\nLevel 03 Unlocked!",Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
            } else if(level.equals("3")) {
                if(correctCount>=4){
                    Toast.makeText(applicationContext, "Congradulations!\nYou Passed this Level!",Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(applicationContext, correctCount.toString() + " correct out of 5",Toast.LENGTH_SHORT).show()
            }
            val myIntent = Intent(this, com.nenasa.dyscalculia.Home::class.java)
            this.startActivity(myIntent)
            finish()
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, com.nenasa.dyscalculia.Home::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}