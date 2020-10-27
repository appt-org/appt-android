package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
data class Article(
    val type: String,
    val id: Int,
    val date: String,
    val modified: String?,
    val title: Content,
    val content: Any?,
    val author: Int?,
    val tags: List<Int>?,
    val categories: List<Int>?,
    val link: String?
) : Serializable