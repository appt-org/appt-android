package nl.appt.model

import nl.appt.database.Page

data class WebPage(
    override val url: String,
    override var title: String?
) : Page