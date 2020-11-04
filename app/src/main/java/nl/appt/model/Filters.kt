package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 30/10/2020
 * Copyright 2020 Stichting Appt
 */
data class Filters(
    val categories: ArrayList<Category>,
    val tags: ArrayList<Tag>,
) : Serializable {

    fun categories(): String? {
        return ids(categories)
    }

    fun tags(): String? {
        return ids(tags)
    }

    private fun ids(list: List<Taxonomy>): String? {
        val ids = list.filter {
            it.selected
        }.map {
            it.id
        }

        if (ids.isEmpty()) {
            return null
        }

        return ids.joinToString(",")
    }
}