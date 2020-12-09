package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 30/10/2020
 * Copyright 2020 Stichting Appt
 */
data class Filters(
    val categories: ArrayList<Category>,
    val tags: ArrayList<Tag>,
) : Serializable