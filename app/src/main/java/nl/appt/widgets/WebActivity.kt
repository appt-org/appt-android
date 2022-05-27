package nl.appt.widgets

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.activity_web.*
import nl.appt.R
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setVisible
import nl.appt.helpers.Events
import nl.appt.helpers.Preferences

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
        // Can be overridden
    }

    override fun onViewCreated() {
        super.onViewCreated()
        setupWebView()
        setupRefresh()
    }

    private fun setupWebView() {
        webView.setBackgroundColor(0x00000000)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.overScrollMode = View.OVER_SCROLL_NEVER

        webView.accessibilityDelegate = View.AccessibilityDelegate()
        webView.webChromeClient = ApptWebChromeClient()
        webView.webViewClient = ApptWebViewClient()

        val settings = webView.settings

        // Apply dark mode based on configuration, because automatic mode doesn't work properly.
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
            }
        }

        // Use custom stylesheet strategy to make sure media query is applied.
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
            WebSettingsCompat.setForceDarkStrategy(settings, WebSettingsCompat.DARK_STRATEGY_WEB_THEME_DARKENING_ONLY)
        }

        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.domStorageEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.mediaPlaybackRequiresUserGesture = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.allowFileAccess = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.setSupportZoom(true)
    }

    private fun setupRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    fun load(content: String, title: String) {
        isLoading = true

        events.log(Events.Category.article, title)

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

    fun load(url: String) {
        webView.loadUrl(url)
    }

    private inner class ApptWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: $url")

            if (url != null) {
                openWebsite(url)
                return true
            }

            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d(TAG, "onPageFinished: $url")

            webView.setVisible(true)
            progressBar.setVisible(false)
            swipeRefreshLayout.isRefreshing = false
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            super.doUpdateVisitedHistory(view, url, isReload)

            Log.d(TAG, "doUpdateVisitedHistory: $url, isReload: $isReload")

            if (url != null) {
                Preferences.setUrl(this@WebActivity, url)
            }
        }
    }

    private inner class ApptWebChromeClient: WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }


    }
}