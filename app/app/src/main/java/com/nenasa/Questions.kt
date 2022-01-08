package com.nenasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import android.widget.RadioButton
import android.widget.RadioGroup


class Questions : AppCompatActivity() {

    lateinit var radioGroup_01: RadioGroup
    lateinit var radioGroup_02: RadioGroup
    lateinit var radioGroup_03: RadioGroup
    lateinit var radioGroup_04: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questions)

        radioGroup_01 = findViewById<RadioGroup>(R.id.radioGroup_01);
        radioGroup_02 = findViewById<RadioGroup>(R.id.radioGroup_02);
        radioGroup_03 = findViewById<RadioGroup>(R.id.radioGroup_03);
        radioGroup_04 = findViewById<RadioGroup>(R.id.radioGroup_04);
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun submitQuestions(view: View){

        var str:String = ""

        val radioGroupId_01  = radioGroup_01.checkedRadioButtonId
        when(radioGroupId_01){
            -1->{
                Toast.makeText(this, "All the questions should be answered!", Toast.LENGTH_SHORT).show()
            }else->{
                val selectedItem = findViewById<RadioButton>(radioGroupId_01)
                str += selectedItem.text
            }
        }

        val radioGroupId_02  = radioGroup_02.checkedRadioButtonId
        when(radioGroupId_02){
            -1->{
                Toast.makeText(this, "All the questions should be answered!", Toast.LENGTH_SHORT).show()
            }else->{
                val selectedItem = findViewById<RadioButton>(radioGroupId_02)
                str += selectedItem.text
            }
        }

        val radioGroupId_03  = radioGroup_03.checkedRadioButtonId
        when(radioGroupId_03){
            -1->{
                Toast.makeText(this, "All the questions should be answered!", Toast.LENGTH_SHORT).show()
            }else->{
                val selectedItem = findViewById<RadioButton>(radioGroupId_03)
                str += selectedItem.text
            }
        }

        val radioGroupId_04  = radioGroup_04.checkedRadioButtonId
        when(radioGroupId_04){
            -1->{
                Toast.makeText(this, "All the questions should be answered!", Toast.LENGTH_SHORT).show()
            }else->{
                val selectedItem = findViewById<RadioButton>(radioGroupId_04)
                str += selectedItem.text
            }
        }

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}