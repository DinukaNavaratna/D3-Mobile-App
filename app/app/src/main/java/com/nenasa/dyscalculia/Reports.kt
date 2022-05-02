package com.nenasa.dyscalculia

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
import com.nenasa.dyscalculia.Home

lateinit var sp: SharedPreference;
lateinit var treatment: String;
var treatment_suffix: String = ""

class Reports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyscalculia_reports)

        treatment_suffix = ""
        val myIntent = intent
        treatment = myIntent.getStringExtra("treatment").toString()
        if(treatment == "true")
            treatment_suffix = "_treatment"

        Toast.makeText(this, "Report: Dyscalculia"+treatment_suffix, Toast.LENGTH_SHORT).show()

        sp = SharedPreference(this)
        var user_id = sp.getPreference("user_id")
        var url = sp.getPreference("ServerHost")
        if (url == null) {
            url = this.getResources().getString(R.string.server_host)
        }

        var dyscalculia_reports_webview = findViewById<WebView>(R.id.dyscalculia_reports_webview);
        var dyscalculia_report_save = findViewById<Button>(R.id.dyscalculia_report_save);

        dyscalculia_reports_webview.webViewClient = WebViewClient()
        dyscalculia_reports_webview.loadUrl(url+"/reports/"+user_id+"/dyscalculia"+treatment_suffix)
        dyscalculia_reports_webview.settings.javaScriptEnabled = true
        dyscalculia_reports_webview.settings.setSupportZoom(true)

        dyscalculia_reports_webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                var dyscalculia_report_loading = findViewById<ProgressBar>(R.id.dyscalculia_report_loading);
                dyscalculia_report_loading.visibility = View.GONE
            }
        })

        dyscalculia_report_save.setOnClickListener(View.OnClickListener {
            if (dyscalculia_reports_webview != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Calling createWebPrintJob()
                    PrintTheWebPage(dyscalculia_reports_webview)
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
        var jobName: String = getString(R.string.app_name) + "__Dyscalculia_Report__"+username;

        var printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName);

        if (printManager != null)
            printJob = printManager.print(jobName, printAdapter, PrintAttributes.Builder().build());
    }

    fun openHome(view: View) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra("treatment", treatment)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }
}