package com.nenasa.dysgraphia

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import com.nenasa.R
import com.nenasa.Services.SharedPreference
import android.widget.Toast

import android.os.Build
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.widget.ProgressBar
import androidx.annotation.RequiresApi

lateinit var sp: SharedPreference;

class Reports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dysgraphia_reports)

        sp = SharedPreference(this)
        var user_id = sp.getPreference("user_id")
        var url = sp.getPreference("ServerHost")
        if (url == null) {
            url = this.getResources().getString(R.string.server_host)
        }

        var dysgraphia_reports_webview = findViewById<WebView>(R.id.dysgraphia_reports_webview);
        var dysgraphia_report_save = findViewById<Button>(R.id.dysgraphia_report_save);

        dysgraphia_reports_webview.webViewClient = WebViewClient()
        dysgraphia_reports_webview.loadUrl(url+"/reports/"+user_id+"/dysgraphia")
        dysgraphia_reports_webview.settings.javaScriptEnabled = true
        dysgraphia_reports_webview.settings.setSupportZoom(true)

        dysgraphia_reports_webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                var dysgraphia_report_loading = findViewById<ProgressBar>(R.id.dysgraphia_report_loading);
                dysgraphia_report_loading.visibility = View.GONE
            }
        })

        dysgraphia_report_save.setOnClickListener(View.OnClickListener {
            if (dysgraphia_reports_webview != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Calling createWebPrintJob()
                    PrintTheWebPage(dysgraphia_reports_webview)
                } else {
                    Toast.makeText(this,"Not available for device below Android LOLLIPOP",Toast.LENGTH_SHORT).show()
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
        var jobName: String = getString(R.string.app_name) + "__Dysgraphia_Report__"+username;

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