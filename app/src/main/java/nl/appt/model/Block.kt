package nl.appt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Block(
    val title: String,
    val description: String,
    val image: String,
    val url: String,
    val type: String,
    val children: ArrayList<Block>
) : Parcelable
