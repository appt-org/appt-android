package nl.appt.model

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Direction(var fingers: Int? = null) {
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    TOP_LEFT,
    UNKNOWN;

    override fun toString(): String {
        return "${super.toString()}($fingers)"
    }
}