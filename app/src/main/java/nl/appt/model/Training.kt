package nl.appt.model

import android.content.Context

/**
 * Created by Jan Jaap de Groot on 20/10/2020
 * Copyright 2020 Stichting Appt
 */
interface Training: Item {
    fun completed(context: Context): Boolean
    fun completed(context: Context, completed: Boolean)
}