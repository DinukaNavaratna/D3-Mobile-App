package com.nenasa.dyslexia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.nenasa.R
import com.nenasa.Services.FileUploadUtility
import com.nenasa.Services.SharedPreference
import java.io.File
import java.io.IOException
import java.util.*
import java.math.BigInteger
import java.security.MessageDigest


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
    lateinit var mediaPlayer: MediaPlayer
    var read_text_arr1: Array<String> = arrayOf("ම", "ල", "ය", "ර", "ප")
    var read_text_arr2: Array<String> = arrayOf("මල", "පය", "මම")
    var read_text_arr3: Array<String> = arrayOf("අම්මා", "අහස", "ලඹය")
    var read_text_arr4: Array<String> = arrayOf("අම්මා උයනවා", "අපි දුවමු", "ලමයා පයිනවා", "ගස අතන", "සල් ගස", "ගී ගයමු", "ලස්සන වත්ත", "අකුරු කියමු", "හොද ලමයා", "සමනලයා පියාබනවා", "ගෙදර යමු")
    var read_text_arr5: Array<String> = arrayOf("අම්මා බත් උයනවා", "අමර සල්ගස යට", "අපි සිංදු කියමු", "ලමයි සිංදු කියනවා", "ඔබ ඔහු සමග", "තාත්තා වැඩට ගියා", "අපි ස්කෝලේ යමු", "මුහුද රැල්ල ලස්සනයි", "රට ලස්සනට තියමු", "අපි අපේම යාලුවෝ")

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
                    if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
                        mediaPlayer.stop()
                    }
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
            if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }
            if(audio != ""){
                frameLayout.visibility = View.VISIBLE;
                try {
                    /*
                    MultipartUtility(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio);
                    */
                    val fileUplaoad = FileUploadUtility(this);
                    //fileUplaoad.doFileUpload(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio)
                    val testUpload = testUpload();
                    Log.d("FileUpload", "Sending the request from Read.kt")
                    testUpload.upload(context, Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio)
                    Log.d("FileUpload", "After sending the request from Read.kt")
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Log.d("FileUpload", "Exception - Line 138 in Read.kt")
                    Log.d("FileUpload", (exception.printStackTrace()).toString())
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
                        } else if(count!! < 30) {
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
        if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
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
        if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
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
        if(level == "Hardපහසු" || level == "Hardමද්\u200Dයම" || level == "Hardඅමාරු"){
            openActivity(Intent(this, com.nenasa.dyslexia.HomeHard::class.java))
        } else {
            openActivity(Intent(this, com.nenasa.dyslexia.HomeEasy::class.java))
        }
    }
    fun openActivity(intent: Intent){
        if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        startActivity(intent)
        finish()
    }

    fun fileUploadResponse(feedback: String){
        frameLayout.visibility = View.GONE;
        if(feedback.contains("failed")){
            Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show()
            next_btn.isEnabled = true
        } else {
            Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show()
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
        if(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        if(levelEnd == true){
            openActivity(Intent(this, com.nenasa.dyslexia.HomeEasy::class.java))
        } else {
            if(level == "Hardපහසු" || level == "Hardමද්\u200Dයම" || level == "Hardඅමාරු"){
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
        } else if(level == "Hardපහසු"){
            readText = read_text_arr4.random()
            read_text_arr4 = remove(read_text_arr4, readText)
            if(read_text_arr4.size == 0 )
                levelEnd = true
        } else if(level == "Hardමද්\u200Dයම"){
            readText = read_text_arr5.random()
            read_text_arr5 = remove(read_text_arr5, readText)
            if(read_text_arr5.size == 0 )
                levelEnd = true
        } else if(level == "Hardඅමාරු"){
            readText = myIntent.getStringExtra("readText").toString()
        }
    }

    fun speak(view: View){
        try {
            val md = MessageDigest.getInstance("MD5")
            val readTextAudio = BigInteger(1, md.digest(readText.toByteArray())).toString(16).padStart(32, '0')
            val uri = "@raw/dyslexia_$readTextAudio"
            val audioResource = resources.getIdentifier(uri, null, packageName)
            mediaPlayer = MediaPlayer.create(this, audioResource)
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop()
            }
            mediaPlayer.start()
        } catch (ex: Exception){
            if(ex.toString().contains("NotFoundException")){
                Toast.makeText(this, "Audio not found!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: $ex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun remove(arr: Array<String>, target: String): Array<String> {
        return arr.filter { key: String -> key != target }
            .toTypedArray()
    }

    override fun onBackPressed() {
    }
}