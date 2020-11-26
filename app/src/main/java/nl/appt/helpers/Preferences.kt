package nl.appt.helpers

import android.content.Context
import android.content.SharedPreferences
import nl.appt.model.Action
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 26/11/2020
 * Copyright 2020 Stichting Appt
 */

class Preferences(val context: Context) {

    private val preferences: SharedPreferences? = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    private fun getBoolean(key: String): Boolean {
        return preferences?.getBoolean(key, false) ?: false
    }

    private fun setBoolean(key: String, value: Boolean) {
        preferences?.edit()?.putBoolean(key, value)?.apply()
    }

    private fun getInt(key: String): Int {
        return preferences?.getInt(key, 0) ?: 0
    }

    private fun setInt(key: String, value: Int) {
        preferences?.edit()?.putInt(key, value)?.apply()
    }

    private fun getString(key: String): String {
        return preferences?.getString(key, "") ?: ""
    }

    private fun setString(key: String, value: String) {
        preferences?.edit()?.putString(key, value)?.apply()
    }

    /* Gesture */

    fun isCompleted(gesture: Gesture): Boolean {
        return getBoolean(gesture.toString())
    }

    fun setCompleted(gesture: Gesture, completed: Boolean) {
        setBoolean(gesture.toString(), completed)
    }

    /* Action */

    fun isCompleted(action: Action): Boolean {
        return getBoolean(action.toString())
    }

    fun setCompleted(action: Action, completed: Boolean) {
        setBoolean(action.toString(), completed)
    }
}