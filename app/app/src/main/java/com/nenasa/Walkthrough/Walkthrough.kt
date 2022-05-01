package com.nenasa.Walkthrough

import android.content.Intent
import android.net.Uri
import android.net.Uri.parse
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.nenasa.Login
import com.nenasa.R

class Walkthrough : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walkthrough)

        //Walkthrough hidden
        //val viewPager = findViewById<ViewPager>(R.id.viewPager)
        //viewPager.adapter = WalkthroughAdapter(supportFragmentManager)

        val videoView = findViewById<VideoView>(R.id.videoView)
        //Creating MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
        val uri: Uri = parse("android.resource://" + getPackageName() + "/" + R.raw.intro)
        //Setting MediaController and URI, then starting the videoView
        //videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }

    fun openLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}