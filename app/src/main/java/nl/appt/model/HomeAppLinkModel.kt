package nl.appt.model

import android.net.Uri

data class HomeAppLinkModel(
    val iconId: Int,
    val title: String,
    val link: Uri
)
