package nl.appt.widgets

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.activity_web.*
import nl.appt.R
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setVisible
import nl.appt.extensions.startActivity
import nl.appt.tabs.knowledge.ArticleActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
@SuppressLint("SetJavaScriptEnabled")
open class WebActivity: ToolbarActivity() {

    private val TAG = "WebActivity"
    private var shareItem: MenuItem? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun getToolbarTitle(): String? {
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)

        shareItem = menu?.findItem(R.id.action_share)
        setShareEnabled(false)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                onShare()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setShareEnabled(enabled: Boolean) {
        shareItem?.isVisible = enabled
    }

    open fun onShare() {

    }

    override fun onViewCreated() {
        super.onViewCreated()
        setupWebView()
    }

    private fun setupWebView() {
        webView.setBackgroundColor(0x00000000)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.accessibilityDelegate = View.AccessibilityDelegate()
        webView.webChromeClient = ApptWebChromeClient()
        webView.webViewClient = ApptWebViewClient()

        val settings = webView.settings

        // Automatic WebView dark mode is buggy, using FORCE_DARK_ON works better.
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
            }
        }
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.setSupportZoom(true)
    }

    fun load(content: String, title: String) {
        setLoading(true)

        val html = """
                <html lang="nl">
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1"/>
                        <link rel="stylesheet" type="text/css" href="style.css">
                    </head>
                <body>
                <h1>
                """ + title + """
                </h1>
                """ + content + """
                </body>
                </html>
        """

        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null)
    }

    inner class ApptWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: $url")

            val uri = Uri.parse(url)
            val segments = uri.pathSegments

            // Check if kennisbank article (appt.nl/kennisbank/slug)
            if (uri.host == "appt.nl" && segments.size == 2 && segments[0] == "kennisbank") {
                // Kennisbank url
                startActivity<ArticleActivity> {
                    putExtra("slug", segments[1])
                }
            } else {
                // Open website
                openWebsite(uri)
            }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            webView.setVisible(true)
            progressBar.setVisible(false)
        }
    }

    inner class ApptWebChromeClient: WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }
}