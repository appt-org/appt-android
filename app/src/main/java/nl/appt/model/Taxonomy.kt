package nl.appt.model

import android.content.Context
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

    override fun title(context: Context) = name

}

typealias Category = Taxonomy
typealias Tag = Taxonomy

val List<Taxonomy>.selected
    get() = filter { it.selected }

val List<Taxonomy>.ids
    get() = map { it.id }

val List<Taxonomy>.names
    get() = map { it.name }