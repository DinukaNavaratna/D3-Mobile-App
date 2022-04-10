package com.nenasa.dyslexia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.nenasa.Nenasa
import com.nenasa.R
import com.nenasa.Services.SharedPreference
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

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
    val nenasa = Nenasa()
    var time_spent: Long = 0
    var recording_time: Long = 0
    lateinit var treatment: String;
    var treatment_suffix: String = ""
    lateinit var sp: SharedPreference
    lateinit var pic: ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nenasa.R.layout.dyslexia_read)

        sp = SharedPreference(this)

        myIntent = intent
        level = myIntent.getStringExtra("level").toString()
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        context = this;
        record_btn = findViewById<Button>(com.nenasa.R.id.record_btn)
        audio_name = findViewById(R.id.audio_name)
        audio_control = findViewById(R.id.audio_control_btn)
        audio_upload = findViewById(R.id.audio_upload)
        frameLayout = findViewById(R.id.frameLayout)
        next_btn = findViewById(R.id.next_btn)
        read_text = findViewById(R.id.read_text)
        micGif = findViewById(R.id.micGif);
        pic = findViewById(R.id.pic);

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
                    output = "$dir/$uuid.wav";
                    audio = "$uuid.wav";
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
                    //val fileUplaoad = FileUploadUtility(this);
                    //fileUplaoad.doFileUpload(Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio)
                    var user_id = sp.getPreference("user_id")
                    val testUpload = testUpload();
                    Log.d("FileUpload", "Sending the request from Read.kt")
                    testUpload.upload(context, Environment.getExternalStorageDirectory().absolutePath+"/Nenasa/"+audio, user_id, level, recording_time.toString(), md5(readText))
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
                        if(count!! < 30) {
                            if(response == "" || response.isNullOrEmpty() || response.equals("pending")) {
                                sp.setPreference("audio_upload_response", "pending")
                                if((count!!.rem(5)) == 0)
                                    Toast.makeText(context, "Audio Processing...", Toast.LENGTH_SHORT).show()
                                count = count?.plus(1)
                                mainHandler.postDelayed(this, 1000)
                            } else {
                                sp.setPreference("audio_upload_response", "")
                                fileUploadResponse(response)
                            }
                        } else {
                            fileUploadResponse("File upload time out!")
                        }
                    }
                })
            }
        }
    }

    private fun startRecording() {
        time_spent = 0
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
                nenasa.showDialogBox(this@Read,"error","Opz!","You ran out of time. Try again!", "null", null, treatment)
            }
        }.start()

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
                recording_time = time_spent
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
        if(feedback.contains("failed") || feedback == ""){
            nenasa.showDialogBox(this, "error", "File Upload Failed", feedback, "null", null, "null")
            Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show()
            Log.e("Audio failed", feedback)
            next_btn.isEnabled = true
        } else {
            //nenasa.showDialogBox(this, "info", "Audio Processed by the Server", feedback, "null", null, "null")
            val percentage = Math.round(feedback.toDouble() * 10.0) / 10.0
            Toast.makeText(this, "Accuracy: "+percentage+"%", Toast.LENGTH_LONG).show()
            Log.e("Audio", "Audio file processed by the server...")
            Log.e("Audio", feedback)
            audio_name.text = "";
            audio_name.visibility = View.INVISIBLE
            audio_control.visibility = View.INVISIBLE
            audio_upload.visibility = View.INVISIBLE
            val coins = (percentage.toFloat() / 10).toInt()
            val score: Int = sp.getPreference("dyslexia_score"+treatment_suffix).toInt() + coins
            sp.setPreference("dyslexia_score"+treatment_suffix, Integer.toString(score))
            if(level.contains("Easy")){
                val score: Int = sp.getPreference("dyslexia_score_easy"+treatment_suffix).toInt() + coins
                sp.setPreference("dyslexia_score_easy"+treatment_suffix, Integer.toString(score))
            } else if(level.contains("Hard")){
                val score: Int = sp.getPreference("dyslexia_score_hard"+treatment_suffix).toInt() + coins
                sp.setPreference("dyslexia_score_hard"+treatment_suffix, Integer.toString(score))
            }
            nextWord()
        }
    }

    fun nextWord(view: View){
        nextWord()
    }

    fun nextWord(){
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

        val uri = "@drawable/dyslexia_" + md5(readText)
        val imageResource = resources.getIdentifier(uri, null, packageName)
        pic.setImageResource(imageResource)
    }

    fun speak(view: View){
        try {
            val uri = "@raw/dyslexia_${md5(readText)}"
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

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getMD5(view: View){
        Log.i("MD5", "ම :"+md5("ම"))
        Log.i("MD5", "ල :"+md5("ල"))
        Log.i("MD5", "ය :"+md5("ය"))
        Log.i("MD5", "ර :"+md5("ර"))
        Log.i("MD5", "ප :"+md5("ප"))
        Log.i("MD5", "මල :"+md5("මල"))
        Log.i("MD5", "පය :"+md5("පය"))
        Log.i("MD5", "මම :"+md5("මම"))
        Log.i("MD5", "අම්මා :"+md5("අම්මා"))
        Log.i("MD5", "අහස :"+md5("අහස"))
        Log.i("MD5", "ලඹය :"+md5("ලඹය"))
        Log.i("MD5", "අම්මා උයනවා :"+md5("අම්මා උයනවා"))
        Log.i("MD5", "අපි දුවමු :"+md5("අපි දුවමු"))
        Log.i("MD5", "ලමයා පයිනවා :"+md5("ලමයා පයිනවා"))
        Log.i("MD5", "ගස අතන :"+md5("ගස අතන"))
        Log.i("MD5", "සල් ගස :"+md5("සල් ගස"))
        Log.i("MD5", "ගී ගයමු :"+md5("ගී ගයමු"))
        Log.i("MD5", "ලස්සන වත්ත :"+md5("ලස්සන වත්ත"))
        Log.i("MD5", "අකුරු කියමු :"+md5("අකුරු කියමු"))
        Log.i("MD5", "හොද ලමයා :"+md5("හොද ලමයා"))
        Log.i("MD5", "සමනලයා පියාබනවා :"+md5("සමනලයා පියාබනවා"))
        Log.i("MD5", "ගෙදර යමු :"+md5("ගෙදර යමු"))
        Log.i("MD5", "අම්මා බත් උයනවා :"+md5("අම්මා බත් උයනවා"))
        Log.i("MD5", "අමර සල්ගස යට :"+md5("අමර සල්ගස යට"))
        Log.i("MD5", "අපි සිංදු කියමු :"+md5("අපි සිංදු කියමු"))
        Log.i("MD5", "ලමයි සිංදු කියනවා :"+md5("ලමයි සිංදු කියනවා"))
        Log.i("MD5", "ඔබ ඔහු සමග :"+md5("ඔබ ඔහු සමග"))
        Log.i("MD5", "තාත්තා වැඩට ගියා :"+md5("තාත්තා වැඩට ගියා"))
        Log.i("MD5", "අපි ස්කෝලේ යමු :"+md5("අපි ස්කෝලේ යමු"))
        Log.i("MD5", "මුහුද රැල්ල ලස්සනයි :"+md5("මුහුද රැල්ල ලස්සනයි"))
        Log.i("MD5", "රට ලස්සනට තියමු :"+md5("රට ලස්සනට තියමු"))
        Log.i("MD5", "අපි අපේම යාලුවෝ :"+md5("අපි අපේම යාලුවෝ"))
    }
}