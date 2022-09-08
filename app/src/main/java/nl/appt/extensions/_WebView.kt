package nl.appt.extensions

import android.webkit.WebBackForwardList
import android.webkit.WebHistoryItem
import android.webkit.WebView
import nl.appt.model.WebPage

/**
 * Convert WebBackForwardList to a list of WebHistoryItem
 */
fun WebBackForwardList.toList(start: Int, limit: Int, step: Int): List<WebHistoryItem> {
    val items = arrayListOf<WebHistoryItem>()

    var i = start + step
    do {
        val item = getItemAtIndex(i)
        items.add(item)
        i += step
    } while (i != limit && i >= 0 && i < this.size)

    return items
}

/**
 * Get list of WebHistoryItem to go back to
 */
fun WebView.getBackList(): List<WebHistoryItem> {
    val list = copyBackForwardList()
    return list.toList(list.currentIndex, 0, -1)
}

/**
 * Get list of WebHistoryItem to go forward to
 */
fun WebView.getForwardList(): List<WebHistoryItem> {
    val list = copyBackForwardList()
    return list.toList(list.currentIndex, list.size, 1)
}

/**
 * Convert WebHistoryItem to WebPage
 */
fun List<WebHistoryItem>.toWebPages(): List<WebPage> {
    return map { item ->
        WebPage(url = item.url, title = item.title)
    }
}