package nl.appt.model

import android.content.Context
import android.graphics.drawable.Drawable

interface Meer : Item {
    fun image(context: Context): Drawable?
}