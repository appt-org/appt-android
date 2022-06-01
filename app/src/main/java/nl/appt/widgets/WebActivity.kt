package nl.appt.widgets

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ShareCompat
import androidx.core.view.children
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
    private val APPT_DOMAIN: String by lazy {
        getString(R.string.appt_domain)
    }
    private val bookmarks = HashMap<String, Boolean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun getToolbarTitle(): String? {
        return null
    }

    override fun onViewCreated() {
        super.onViewCreated()
        setupActions()
        setupWebView()
        setupRefresh()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupActions() {
        actionLayout.children.forEach { view ->
            TooltipCompat.setTooltipText(view, view.contentDescription)
        }

        backButton.setOnClickListener { onBack() }
        forwardButton.setOnClickListener { onForward() }
        shareButton.setOnClickListener { onShare() }
        bookmarkButton.setOnClickListener { onBookmark() }
        menuButton.setOnClickListener { onMenu() }
    }

    private fun onBack() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }

    private fun onForward() {
        if (webView.canGoForward()) {
            webView.goForward()
        }
    }

    private fun onShare() {
        webView.url?.let { url ->
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.action_share)
                .setText(url)
                .startChooser()
        }
    }

    private fun onBookmark() {
        webView.url?.let { url ->
            val bookmarked = !bookmarks.getOrDefault(url, false)
            bookmarks[url] = bookmarked

            updateBookmark(bookmarked)
        }
    }

    private fun updateBookmark(url: String) {
        val bookmarked = bookmarks.getOrDefault(url, false)
        updateBookmark(bookmarked)
    }

    private fun updateBookmark(bookmarked: Boolean) {
        val icon = if (bookmarked) R.drawable.icon_bookmarked else R.drawable.icon_bookmark
        val text = if (bookmarked) R.string.action_bookmarked else R.string.action_bookmark

        bookmarkButton.setImageResource(icon)
        bookmarkButton.contentDescription = getString(text)
        TooltipCompat.setTooltipText(bookmarkButton, bookmarkButton.contentDescription)
    }

    private fun onMenu() {
        toast(R.string.action_menu)
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

    fun load(url: String) {
        webView.loadUrl(url)
    }

    private inner class ApptWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: $url")

            if (url == null) {
                return false
            }

            val uri = Uri.parse(url)
            if (uri.host == APPT_DOMAIN) {
                return false
            }

            openWebsite(url)
            return true
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

            backButton.isEnabled = webView.canGoBack()
            forwardButton.isEnabled = webView.canGoForward()

            if (url != null) {
                shareButton.isEnabled = true
                bookmarkButton.isEnabled = true
                updateBookmark(url)

                Preferences.setUrl(this@WebActivity, url)
                events.log(Events.Category.url_change, url)
            }
        }
    }

    private inner class ApptWebChromeClient: WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }
}