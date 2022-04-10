package com.nenasa.dyslexia

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.nenasa.R
import com.nenasa.Services.SharedPreference

lateinit var sp: SharedPreference;
lateinit var level: String

class Reports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_reports)

        sp = SharedPreference(this)
        var user_id = sp.getPreference("user_id")
        var url = sp.getPreference("ServerHost")
        if (url == null) {
            url = this.getResources().getString(R.string.server_host)
        }

        val myIntent = intent
        level = myIntent.getStringExtra("level").toString()

        var dyslexia_reports_webview = findViewById<WebView>(R.id.dyslexia_reports_webview);
        var dyslexia_report_save = findViewById<Button>(R.id.dyslexia_report_save);

        dyslexia_reports_webview.webViewClient = WebViewClient()
        dyslexia_reports_webview.loadUrl(url+"/reports/1/dyslexia_"+level)
        dyslexia_reports_webview.settings.javaScriptEnabled = true
        dyslexia_reports_webview.settings.setSupportZoom(true)

        dyslexia_reports_webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                var dyslexia_report_loading = findViewById<ProgressBar>(R.id.dyslexia_report_loading);
                dyslexia_report_loading.visibility = View.GONE
            }
        })

        dyslexia_report_save.setOnClickListener(View.OnClickListener {
            if (dyslexia_reports_webview != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Calling createWebPrintJob()
                    PrintTheWebPage(dyslexia_reports_webview)
                } else {
                    Toast.makeText(this,"Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show()
            }
        })
    }

    lateinit var printJob: PrintJob;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun PrintTheWebPage(webView: WebView) {
        var printManager: PrintManager =
            this.getSystemService(Context.PRINT_SERVICE) as PrintManager;

        var username = sp.getPreference("username")
        var jobName: String = getString(R.string.app_name) + "__dyslexia_"+level+"_Report__"+username;

        var printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName);

        if (printManager != null)
            printJob = printManager.print(jobName, printAdapter, PrintAttributes.Builder().build());
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}