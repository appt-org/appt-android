package nl.appt.model

import java.io.Serializable

data class Block(
    val title: String,
    val description: String,
    val image: String,
    val url: String,
    val type: String,
    val children: ArrayList<Block>
) : Serializable
