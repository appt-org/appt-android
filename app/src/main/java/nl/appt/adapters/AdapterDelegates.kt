package nl.appt.adapters

import android.widget.CheckBox
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import nl.appt.R
import nl.appt.model.Article
import nl.appt.model.Gesture
import nl.appt.model.Item
import nl.appt.model.Taxonomy

/**
 * Created by Jan Jaap de Groot on 03/11/2020
 * Copyright 2020 Stichting Appt
 */

fun headerAdapterDelegate() = adapterDelegate<String, Any>(R.layout.view_header) {
    val header: TextView = findViewById(R.id.headerView)

    bind {
        header.text = item
    }
}

private inline fun <reified T: Item> itemAdapterDelegate(crossinline callback : (T) -> Unit) = adapterDelegate<T, Any>(R.layout.view_item) {
    val textView: TextView = findViewById(R.id.textView)

    textView.setOnClickListener {
        callback(item)
    }

    bind {
        textView.text = item.title()
    }
}

fun gestureAdapterDelegate(callback: (Gesture) -> Unit): AdapterDelegate<List<Any>> {
    return itemAdapterDelegate(callback)
}

fun articleAdapterDelegate(callback: (Article) -> Unit): AdapterDelegate<List<Any>> {
    return itemAdapterDelegate(callback)
}

fun taxonomyAdapterDelegate(callback: (Taxonomy) -> Unit) = adapterDelegate<Taxonomy, Any>(R.layout.view_checkbox) {
    val checkBox: CheckBox = findViewById(R.id.checkBox)

    checkBox.setOnCheckedChangeListener { _, checked ->
        item.selected = !item.selected
        callback(item)
    }

    bind {
        checkBox.text = item.title()
        checkBox.isChecked = item.selected
    }
}