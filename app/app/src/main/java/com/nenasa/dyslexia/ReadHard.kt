package com.nenasa.dyslexia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.nenasa.R
import com.nenasa.databinding.DyslexiaReadHardBinding
import com.nenasa.dyslexia.adapter.SentenceAdapter
import com.nenasa.dyslexia.adapter.WordsAdapter
import com.nenasa.dyslexia.callback.DropListener

class ReadHard : AppCompatActivity() {

    private lateinit var level: String;
    private lateinit var readText: String;
    private var levelEnd: Boolean? = false
    private lateinit var hard_word_text: TextView;
    private lateinit var newText: String;

    var read_text_arr4: Array<String> = arrayOf("අම්ම උයනවා", "අපි දුවමු", "ලමයා පයිනවා", "ගස අතන", "සල් ගස", "ගී ගයමු", "ලස්සන වත්ත", "අකුරු කියමු", "හොද ලමයා", "සමනලයා පියාබනවා", "ගෙදට යමු")
    var read_text_arr5: Array<String> = arrayOf("අම්මා බත් උයනවා", "අමර සල්ගස යට", "අපි සිංදු කියමු", "ලමයි සිංදු කියනවා", "ඔබ ඔහු සමග", "තාත්තා වැඩට ගියා", "අපි ස්කෝලේ යමු", "මුහුද රැල්ල ලස්සනයි", "රට ලස්සනට තියමු", "අපි අපේම යාලුවෝ")

    // values of the draggable views (usually this should come from a data source)
    private lateinit var words: MutableList<String>
    // last selected word
    private var selectedWord = ""

    private val binding by lazy { DyslexiaReadHardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myIntent = intent
        level = myIntent.getStringExtra("level").toString()

        if(level == "Hardපහසු"){
            readText = read_text_arr4.random()
            read_text_arr4 = remove(read_text_arr4, readText)
            if(read_text_arr4.size == 0 )
                levelEnd = true
        } else if(level == "Hardඅමාරු") {
            readText = read_text_arr5.random()
            read_text_arr5 = remove(read_text_arr5, readText)
            if(read_text_arr5.size == 0 )
                levelEnd = true
        }

        val wordString = readText+" "+read_text_arr4.random()+" "+read_text_arr5.random();
        val list = wordString.split(" ")
        words = list.shuffled() as MutableList<String>

        hard_word_text = findViewById(R.id.hard_word_text)
        hard_word_text.text = readText.toString()

        set_words()
    }

    fun set_words(){
        newText = "";
        val sentenceAdapter = SentenceAdapter()
        val wordsAdapter = WordsAdapter {
            selectedWord = it
        }.apply {
            submitList(words)
        }

        binding.rvSentence.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSentence.adapter = sentenceAdapter

        binding.rvSentence.setOnDragListener(
            DropListener {
                wordsAdapter.removeItem(selectedWord)
                sentenceAdapter.addItem(selectedWord)
                if(newText == ""){
                    newText += selectedWord.toString()
                } else {
                    newText += " $selectedWord"
                }
            }
        )

        binding.rvWords.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP).apply {
            justifyContent = JustifyContent.SPACE_EVENLY
            alignItems = AlignItems.CENTER
        }

        binding.rvWords.adapter = wordsAdapter
    }

    fun remove(arr: Array<String>, target: String): Array<String> {
        return arr.filter { key: String -> key != target }
            .toTypedArray()
    }

    fun openHome(view: View) {
        openHome()
    }
    fun openHome(){
        val intent = Intent(this, HomeHard::class.java)
        startActivity(intent)
        finish()
    }

    fun retry(view: View){
        set_words();
    }
    
    fun next(view: View){
        if(readText == newText){
            val intent = Intent(this, Read::class.java)
            intent.putExtra("level", level)
            intent.putExtra("readText", readText)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "ඔබ කළ වාක්\u200Dයය වැරදියි. කරුණාකර නැවත උත්සාහ කරන්න!", Toast.LENGTH_SHORT).show()
            set_words();
        }
    }

    override fun onBackPressed() {
    }
}