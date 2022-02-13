package com.nenasa.dyslexia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.view.View
import java.io.IOException
import android.view.View.OnTouchListener
import java.io.File
import java.lang.Exception
import java.util.*
import android.media.AudioAttributes

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.nenasa.R
import com.nenasa.Services.FileUploadUtility
import com.nenasa.Services.SharedPreference
import java.lang.IllegalArgumentException


class Read : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    lateinit var audio_name: TextView
    lateinit var audio_control: ImageView
    lateinit var audio_upload: Button
    private var audioPlaying: Boolean? = false
    private var audio: String? = ""
    lateinit var frameLayout: FrameLayout;
    lateinit var record_btn: Button
    lateinit var next_btn: Button
    private lateinit var context: Context;
    private lateinit var readText: String;
    private lateinit var read_text: TextView;
    private lateinit var level: String;
    private var levelEnd: Boolean? = false
    lateinit var micGif: ConstraintLayout;
    lateinit var myIntent: Intent;
    var read_text_arr1: Array<String> = arrayOf("ම", "ල", "ය", "ර", "ප")
    var read_text_arr2: Array<String> = arrayOf("මල", "පය", "මම")
    var read_text_arr3: Array<String> = arrayOf("අම්මා", "අහස", "ලඹය")

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nenasa.R.layout.dyslexia_read)

        myIntent = intent
        level = myIntent.getStringExtra("level").toString()

        context = this;
        record_btn = findViewById<Button>(com.nenasa.R.id.record_btn)
        audio_name = findViewById(R.id.audio_name)
        audio_control = findViewById(R.id.audio_control_btn)
        audio_upload = findViewById(R.id.audio_upload)
        frameLayout = findViewById(R.id.frameLayout)
        next_btn = findViewById(R.id.next_btn)
        read_text = findViewById(R.id.read_text)
        micGif = findViewById(R.id.micGif);

        getReadText();
        read_text.text = readText.toString()

        record_btn.setOnTouchListener(OnTouchListener {v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mediaRecorder = MediaRecorder()
                    mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                    val uuid: String = UUID.randomUUID().toString()
                    val dir = Environment.getExternalStorageDirectory().absolutePath+"/Nenasa";
                    File(dir).mkdirs();
                    output = "$dir/$uuid.mp3";
                    audio = "$uuid.mp3";
                    mediaRecorder?.setOutputFile(output)

                    if(audioPlaying == true){
                        stopPlaying()
                    }
                    audio_name.text = "";
                    audio_name.visibility = View.INVISIBLE
                    audio_control.visibility = View.INVISIBLE
                    audio_upload.visibility = View.INVISIBLE
                    startRecording();
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    stopRecording();
                }
            }
            false
        })

        audio_control.setOnClickListener {
            if(audioPlaying == true){
                stopPlaying()
            } else {
                startPlaying(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio)
            }
        }

        audio_upload.setOnClickListener {
            if(audio != ""){
                frameLayout.visibility = View.VISIBLE;
                try {
                    /*
                    MultipartUtility(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio);
                    */
                    val fileUplaoad = FileUploadUtility(this);
                    fileUplaoad.doFileUpload(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                val mainHandler = Handler(Looper.getMainLooper())
                var count: Int? = 0;
                mainHandler.post(object : Runnable {
                    override fun run() {
                        val sp = SharedPreference(context)
                        var response = sp.getPreference("audio_upload_response")
                        if(response != "" && !response.isNullOrEmpty() && !response.equals("pending")) {
                            sp.setPreference("audio_upload_response", "pending")
                            fileUploadResponse(response)
                        } else if(count!! < 10) {
                            if((count!!.rem(5)) == 0)
                                Toast.makeText(context, "File uploading...", Toast.LENGTH_SHORT).show()
                            count = count?.plus(1)
                            mainHandler.postDelayed(this, 1000)
                        } else {
                            fileUploadResponse("File upload time out!")
                        }
                    }
                })
            }
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            micGif.visibility = View.VISIBLE
            record_btn.visibility = View.INVISIBLE
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            micGif.visibility = View.INVISIBLE
            record_btn.visibility = View.VISIBLE
            Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            micGif.visibility = View.INVISIBLE
            record_btn.visibility = View.VISIBLE
            Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording(){
        if(state){
            try {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                state = false
                //Toast.makeText(this, "Recording ended!", Toast.LENGTH_SHORT).show()
                audio_name.text = audio.toString()
                audio_name.visibility = View.VISIBLE
                audio_control.visibility = View.VISIBLE
                audio_upload.visibility = View.VISIBLE
                micGif.visibility = View.INVISIBLE
                record_btn.visibility = View.VISIBLE
            } catch (e: Exception) {
                micGif.visibility = View.INVISIBLE
                record_btn.visibility = View.VISIBLE
                Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    protected var player: MediaPlayer? = null

    private fun startPlaying(filePath: String) {
        player = MediaPlayer()
        try {
            player!!.setDataSource(filePath) // pass reference to file to be played
            player!!.setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            ) // optional step
            player!!.prepare() // may take a while depending on the media, consider using .prepareAsync() for streaming
        } catch (e: IOException) { // we need to catch both errors in case of invalid or inaccessible resources
            // handle error
        } catch (e: IllegalArgumentException) {
            // handle error
        }
        player!!.start()
        audioPlaying = true
        audio_control.setImageResource(R.drawable.ic_baseline_stop_circle_24);
    }

    private fun stopPlaying() {
        player!!.stop()
        player!!.release() // free up resources
        player = null
        audioPlaying = false
        audio_control.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
    }

    fun openHome(view: View) {
        if(level == "Hardපහසු" || level == "Hardඅමාරු"){
            openActivity(Intent(this, com.nenasa.dyslexia.HomeHard::class.java))
        } else {
            openActivity(Intent(this, com.nenasa.dyslexia.HomeEasy::class.java))
        }
    }
    fun openActivity(intent: Intent){
        startActivity(intent)
        finish()
    }

    fun fileUploadResponse(feedback: String){
        frameLayout.visibility = View.GONE;
        if(feedback.contains("failed")){
            Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show()
            next_btn.isEnabled = true
        } else {
            Toast.makeText(this, "Audio file processed by the server...", Toast.LENGTH_SHORT).show()
            next_btn.isEnabled = true
            audio_name.text = "";
            audio_name.visibility = View.INVISIBLE
            audio_control.visibility = View.INVISIBLE
            audio_upload.visibility = View.INVISIBLE
            record_btn.isEnabled = false
        }
    }

    fun nextWord(view: View){
        if(levelEnd == true){
            openActivity(Intent(this, com.nenasa.dyslexia.HomeEasy::class.java))
        } else {
            if(level == "Hardපහසු" || level == "Hardඅමාරු"){
                openActivity(Intent(this, com.nenasa.dyslexia.HomeHard::class.java))
            } else {
                getReadText()
                read_text.text = readText
                record_btn.isEnabled = true
                next_btn.isEnabled = false
            }
        }
    }

    fun getReadText(){
        if(level == "Easyපහසු"){
            readText = read_text_arr1.random()
            read_text_arr1 = remove(read_text_arr1, readText)
            if(read_text_arr1.size == 0 )
                levelEnd = true
        } else if(level == "Easyමද්\u200Dයම"){
            readText = read_text_arr2.random()
            read_text_arr2 = remove(read_text_arr2, readText)
            if(read_text_arr2.size == 0 )
                levelEnd = true
        } else if(level == "Easyඅමාරු"){
            readText = read_text_arr3.random()
            read_text_arr3 = remove(read_text_arr3, readText)
            if(read_text_arr3.size == 0 )
                levelEnd = true
        } else if(level == "Hardපහසු" || level == "Hardඅමාරු"){
            readText = myIntent.getStringExtra("readText").toString()
        }
    }

    fun findIndex(arr: Array<String>, item: String): Int {
        return arr.indexOf(item)
    }

    fun remove(arr: Array<String>, target: String): Array<String> {
        return arr.filter { key: String -> key != target }
            .toTypedArray()
    }

    override fun onBackPressed() {
    }
}