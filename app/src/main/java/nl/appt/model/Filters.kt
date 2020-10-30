package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 30/10/2020
 * Copyright 2020 Stichting Appt
 */
data class Filters(
    val categories: List<Category>,
    val tags: List<Tag>,
) : Serializable