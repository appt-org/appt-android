package nl.appt.model

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Direction(var fingers: Int? = null) {
    UP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    DOWN,
    BOTTOM_LEFT,
    LEFT,
    TOP_LEFT,
    UNKNOWN;

    val title: String
        get() {
            return when (this) {
                UP -> "omhoog"
                TOP_RIGHT -> "schuin omhoog naar rechts"
                RIGHT -> "naar rechts"
                BOTTOM_RIGHT -> "schuin omlaag naar rechts"
                DOWN -> "omlaag"
                BOTTOM_LEFT -> "schuin omlaag naar links"
                LEFT -> "naar links"
                TOP_LEFT -> "schuin omhoog naar links"
                UNKNOWN -> "onbekend"
            }
        }

    override fun toString(): String {
        return "${super.toString()}($fingers)"
    }

    companion object {
        fun feedback(vararg directions: Direction): String {
            return directions.joinToString(separator = " en dan ") { it.title }
        }
    }
}