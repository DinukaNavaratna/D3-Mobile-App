package com.nenasa

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nenasa.Services.SharedPreference

class Home : AppCompatActivity() {

    lateinit var dyslexia_btn: Button
    lateinit var dysgraphia_btn: Button
    lateinit var dyscalculia_btn: Button
    lateinit var sp: SharedPreference
    lateinit var change_selection_btn: Button
    lateinit var not_sure_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        sp = SharedPreference(this)
        var username: TextView = findViewById(R.id.username)
        dyslexia_btn = findViewById<Button>(R.id.dyslexia_btn)
        dysgraphia_btn = findViewById<Button>(R.id.dysgraphia_btn)
        dyscalculia_btn = findViewById<Button>(R.id.dyscalculia_btn)
        change_selection_btn = findViewById<Button>(R.id.change_selection_btn)
        not_sure_btn = findViewById<Button>(R.id.not_sure_btn)

        username.setText(sp.getPreference("username"))

        var selection = sp.getPreference("selection")
        if(selection != "" && !selection.isNullOrEmpty() && selection != "none")
            selectionButtonOptions(selection)

        dyslexia_btn.setOnClickListener {
            sp.setPreference("selection", "dyslexia")
            selectionButtonOptions("dyslexia")
            val intent = Intent(this, com.nenasa.dyslexia.Home::class.java)
            startActivity(intent)
            finish()
        }
        dysgraphia_btn.setOnClickListener {
            sp.setPreference("selection", "dysgraphia")
            selectionButtonOptions("dysgraphia")
            val intent = Intent(this, com.nenasa.dysgraphia.Home::class.java)
            startActivity(intent)
            finish()
        }
        dyscalculia_btn.setOnClickListener {
            sp.setPreference("selection", "dyscalculia")
            selectionButtonOptions("dyscalculia")
            val intent = Intent(this, com.nenasa.dyscalculia.Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun openQuestions(view: View) {
        val intent = Intent(this, Questions::class.java)
        startActivity(intent)
        finish()
    }

    fun selectionButtonOptions(selection: String) {
        change_selection_btn.visibility = View.VISIBLE
        not_sure_btn.visibility = View.GONE

        if(selection == "dysgraphia"){
            dyscalculia_btn.isEnabled = false
            dyscalculia_btn.setAlpha(0.5F)
            dyslexia_btn.isEnabled = false
            dyslexia_btn.setAlpha(0.5F)
        } else if(selection == "dyslexia"){
            dyscalculia_btn.isEnabled = false
            dyscalculia_btn.setAlpha(0.5F)
            dysgraphia_btn.isEnabled = false
            dysgraphia_btn.setAlpha(0.5F)
        } else if(selection == "dyscalculia"){
            dyslexia_btn.isEnabled = false
            dyslexia_btn.setAlpha(0.5F)
            dysgraphia_btn.isEnabled = false
            dysgraphia_btn.setAlpha(0.5F)
        }

    }

    fun clearSelection(view: View){
        sp.removePreference("selection")
        dyslexia_btn.isEnabled = true
        dyslexia_btn.setAlpha(1.0F)
        dysgraphia_btn.isEnabled = true
        dysgraphia_btn.setAlpha(1.0F)
        dyscalculia_btn.isEnabled = true
        dyscalculia_btn.setAlpha(1.0F)
        change_selection_btn.visibility = View.GONE
        not_sure_btn.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this, Hidden::class.java)
            startActivity(intent)
            finish()
        }
        return super.onKeyLongPress(keyCode, event)
    }

}