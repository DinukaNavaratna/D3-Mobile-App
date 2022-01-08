package com.nenasa.dyslexia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.view.View
import android.widget.Toast
import java.io.IOException
import android.view.View.OnTouchListener
import android.widget.Button
import com.nenasa.Home
import java.io.File
import java.lang.Exception
import java.util.*

class Read : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nenasa.R.layout.dyslexia_read)

        val record_btn = findViewById<Button>(com.nenasa.R.id.record_btn)

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
                    mediaRecorder?.setOutputFile(output)

                    startRecording();
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    stopRecording();
                }
            }
            false
        })
    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording(){
        if(state){
            try {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                state = false
                Toast.makeText(this, "Recording ended!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    fun openHome(view: View) {
        val intent = Intent(this, com.nenasa.dyslexia.Home::class.java)
        startActivity(intent)
        finish()
    }
}