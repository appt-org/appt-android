package nl.appt.helpers

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Jan Jaap de Groot on 27/05/2022
 * Copyright 2022 Stichting Appt
 */
object Preferences {

    private var preferences: SharedPreferences? = null

    private fun preferences(context: Context): SharedPreferences {
        preferences?.let {
            return@preferences it
        }
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    private fun getBoolean(context: Context, key: String): Boolean {
        return preferences(context).getBoolean(key, false)
    }

    private fun setBoolean(context: Context, key: String, value: Boolean) {
        preferences(context).edit()?.putBoolean(key, value)?.apply()
    }

    private fun getInt(context: Context, key: String): Int {
        return preferences(context).getInt(key, -1)
    }

    private fun setInt(context: Context, key: String, value: Int) {
        preferences(context).edit()?.putInt(key, value)?.apply()
    }

    private fun getString(context: Context, key: String): String? {
        return preferences(context).getString(key, null)
    }

    private fun setString(context: Context, key: String, value: String) {
        preferences(context).edit()?.putString(key, value)?.apply()
    }

    /* URL */

    private const val KEY_URL = "url"

    fun getUrl(context: Context): String? {
        return getString(context, KEY_URL)
    }

    fun setUrl(context: Context, url: String) {
        setString(context, KEY_URL, url)
    }
}