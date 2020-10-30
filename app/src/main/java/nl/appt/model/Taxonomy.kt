package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
open class Taxonomy(
    val id: Int,
    val count: Int,
    val name: String,
    val description: String,
    var selected: Boolean = false
) : Item, Serializable {

    override fun title() = name

}

typealias Category = Taxonomy
typealias Tag = Taxonomy