package nl.appt.helpers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import nl.appt.extensions.identifier

/**
 * Created by Jan Jaap de Groot on 09/12/2020
 * Copyright 2020 Stichting Appt
 */
class Events(private val firebaseAnalytics: FirebaseAnalytics) {

    enum class Property {
        talkback
    }

    fun property(property: Property, value: String) {
        firebaseAnalytics.setUserProperty(property.identifier, value)
    }

    fun property(property: Property, value: Boolean) {
        property(property, if (value) "1" else "0")
    }

    enum class Category {
        article,
        filters,
        actionCompleted,
        gestureCompleted
    }

    fun log(category: Category, identifier: String, value: Int? = null) {
        print("Log event, category: $category, identifier: $identifier, value: $value")

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, identifier)

        if (value != null) {
            bundle.putInt(FirebaseAnalytics.Param.VALUE, value)
        }

        firebaseAnalytics.logEvent(category.identifier, bundle)
    }

    fun log(category: Category, data: Any) {
        val json = Gson().toJson(data)
        log(category, json)
    }
}