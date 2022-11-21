package nl.appt.widgets

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ShareCompat
import androidx.core.view.children
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.activity_web.*
import nl.appt.R
import nl.appt.database.Bookmark
import nl.appt.database.History
import nl.appt.dialog.*
import nl.appt.extensions.*
import nl.appt.helpers.Events
import nl.appt.helpers.Preferences
import nl.appt.model.Item
import nl.appt.model.WebPage
import nl.appt.model.WebViewModel

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2022 Stichting Appt
 */
@SuppressLint("SetJavaScriptEnabled")
open class WebActivity: ToolbarActivity() {

    private val TAG = "WebActivity"

    private val APPT_DOMAIN: String by lazy {
        getString(R.string.appt_domain)
    }

    private val viewModel: WebViewModel by viewModels()

    private var initialScale = 1.0f

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
        // Tooltips
        actionLayout.children.forEach { view ->
            TooltipCompat.setTooltipText(view, view.contentDescription)
        }

        // Back
        backButton.setOnClickListener {
            onBack()
        }
        backButton.setOnLongClickListener {
            showBack()
            true
        }

        // Forward
        forwardButton.setOnClickListener {
            onForward()
        }
        forwardButton.setOnLongClickListener {
            showForward()
            true
        }

        // Share
        shareButton.setOnClickListener {
            onShare()
        }

        // Bookmark
        bookmarkButton.setOnClickListener {
            onBookmarked()
        }
        bookmarkButton.setOnLongClickListener {
            showBookmarks()
            true
        }

        // More
        moreButton.setOnClickListener {
            showMenu()
        }
    }

    private fun onBack() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }

    private fun showBack() {
        val pages = webView.getBackList().toWebPages()
        showPages(Item.JUMP_BACK, pages)
    }

    private fun onForward() {
        if (webView.canGoForward()) {
            webView.goForward()
        }
    }

    private fun showForward() {
        val pages = webView.getForwardList().toWebPages()
        showPages(Item.JUMP_FORWARD, pages)
    }

    private fun onShare() {
        webView.url?.let { url ->
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
        }
    }

    private fun onBookmarked() {
        webView.url?.let { url ->
            viewModel.getBookmark(url).observeOnce(this) { bookmark ->
                if (bookmark != null) {
                    viewModel.deleteBookmark(bookmark)
                    updateBookmarkState(false)
                } else {
                    val newBookmark = Bookmark(url = url, title = webView.title)
                    viewModel.insertBookmark(newBookmark)
                    updateBookmarkState(true)
                }
            }
        }
    }

    private fun onUrlChanged(url: String ) {
        val page = History(url = url, title = null)
        viewModel.insertHistory(page)

        viewModel.getBookmark(url).observe(this) { bookmark ->
            updateBookmarkState(bookmark != null)
        }
    }

    private fun updateBookmarkState(bookmarked: Boolean) {
        val icon = if (bookmarked) R.drawable.icon_bookmarked else R.drawable.icon_bookmark
        val label = if (bookmarked) R.string.bookmarked else R.string.bookmark

        bookmarkButton.setImageResource(icon)
        bookmarkButton.contentDescription = getString(label)
        TooltipCompat.setTooltipText(bookmarkButton, bookmarkButton.contentDescription)
    }

    private fun showMenu() {
        val items = listOf(
            Item.HOME,
            Item.RELOAD,
            Item.BOOKMARKS,
            Item.HISTORY,
            Item.SETTINGS,
            Item.CLOSE
        )

        val dialog = ItemsDialog(items)
        dialog.callback = { item ->
            dialog.dismiss()

            when (item) {
                Item.HOME -> load(getString(R.string.appt_url))
                Item.RELOAD -> webView.reload()
                Item.BOOKMARKS -> showBookmarks()
                Item.HISTORY -> showHistory()
                Item.SETTINGS -> showSettings()
                Item.CLOSE -> {
                    // Ignored
                }
                else -> {
                    toast("Not implemented")
                }
            }
        }
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showBookmarks() {
        val dialog = BookmarksDialog()
        dialog.onClick = { bookmark ->
            dialog.dismiss()
            load(bookmark.url)
        }
        dialog.onLongClick = { bookmark ->
            // TODO: Remove from bookmarks
        }
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showHistory() {
        val dialog = HistoryDialog()
        dialog.onClick = { history ->
            dialog.dismiss()
            load(history.url)
        }
        dialog.onLongClick = { history ->
            // TODO: Remove from history
        }
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showPages(item: Item, pages: List<WebPage>) {
        val dialog = WebPageDialog(item, pages)
        dialog.onClick = { history ->
            dialog.dismiss()
            load(history.url)
        }
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showSettings() {
        val dialog = SettingsDialog()

        dialog.zoomScaleCallback = { zoomScale ->
            webView.setScale(initialScale, zoomScale)
            Preferences.setZoomScale(this, zoomScale)
        }

        dialog.show(supportFragmentManager, dialog.tag)
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
        settings.useWideViewPort = false
        settings.loadWithOverviewMode = false

        // Store initial scale, set zoom scale
        this.initialScale = webView.scale
        val zoomScale = Preferences.getZoomScale(this)
        webView.setScale(initialScale, zoomScale)
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

        private var previousUrl: String? = null

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return shouldOverrideUrl(url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return shouldOverrideUrl(request?.url.toString())
        }

        private fun shouldOverrideUrl(url: String?): Boolean {
            Log.d(TAG, "shouldOverrideUrl: $url")

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

            webView.visible = true
            progressBar.visible = false
            swipeRefreshLayout.isRefreshing = false
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            super.doUpdateVisitedHistory(view, url, isReload)

            Log.d(TAG, "doUpdateVisitedHistory: $url, isReload: $isReload")

            backButton.isEnabled = webView.canGoBack()
            forwardButton.isEnabled = webView.canGoForward()

            if (url != null && url != previousUrl) {
                shareButton.isEnabled = true
                bookmarkButton.isEnabled = true
                onUrlChanged(url)

                Preferences.setUrl(this@WebActivity, url)
                events.log(Events.Category.url_change, url)

                previousUrl = url
            }
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            onError(errorResponse?.statusCode)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            onError(error?.errorCode)
        }

        private var showingError = false

        private fun onError(code: Int?) {
            Log.d(TAG, "onError: $code")

            if (code == 200 || code == 301 || code == 302) {
                return
            }

            val message = when (code) {
                ERROR_AUTHENTICATION -> R.string.error_something
                ERROR_BAD_URL -> R.string.error_something
                ERROR_CONNECT -> R.string.error_something
                ERROR_FAILED_SSL_HANDSHAKE -> R.string.error_something
                ERROR_FILE,ERROR_FILE_NOT_FOUND -> R.string.error_something
                ERROR_HOST_LOOKUP -> R.string.error_network
                ERROR_IO -> R.string.error_something
                ERROR_PROXY_AUTHENTICATION -> R.string.error_something
                ERROR_REDIRECT_LOOP -> R.string.error_something
                ERROR_TIMEOUT -> R.string.error_something
                ERROR_TOO_MANY_REQUESTS -> R.string.error_something
                ERROR_UNKNOWN -> R.string.error_something
                ERROR_UNSAFE_RESOURCE -> R.string.error_something
                ERROR_UNSUPPORTED_AUTH_SCHEME -> R.string.error_something
                ERROR_UNSUPPORTED_SCHEME -> R.string.error_something
                SAFE_BROWSING_THREAT_BILLING -> R.string.error_something
                SAFE_BROWSING_THREAT_MALWARE -> R.string.error_something
                SAFE_BROWSING_THREAT_PHISHING -> R.string.error_something
                SAFE_BROWSING_THREAT_UNKNOWN -> R.string.error_something
                SAFE_BROWSING_THREAT_UNWANTED_SOFTWARE -> R.string.error_something
                else -> {
                    if (code == 404) {
                        R.string.error_404
                    } else if ((500 .. 600).contains(code)) {
                        R.string.error_server
                    } else {
                        R.string.error_something
                    }
                }
            }

            if (showingError) {
                return
            }

            showingError = true

            showError(message) {
                showingError = false
            }
        }
    }

    private inner class ApptWebChromeClient: WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            Log.d(TAG, "onProgressChanged: $newProgress")
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            Log.d(TAG, "onReceivedTitle: $title")

            if (title == null) {
                return
            }

            webView.url?.let { url ->
                viewModel.getHistory(url).observeOnce { history ->
                    if (history != null && history.title != title) {
                        Log.d(TAG, "Updating history title")
                        history.title = title
                        viewModel.updateHistory(history)
                    }
                }
            }
        }
    }
}