package nl.appt.extensions

import android.view.View

/**
 * Created by Jan Jaap de Groot on 29/10/2020
 * Copyright 2020 Stichting Appt
 */

var View.visible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }