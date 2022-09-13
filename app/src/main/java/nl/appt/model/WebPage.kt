package nl.appt.model

import android.webkit.WebHistoryItem
import nl.appt.database.Page

data class WebPage(
    override val url: String,
    override var title: String?
) : Page {
    constructor(item: WebHistoryItem) : this(
        url = item.url,
        title = item.title
    )
}