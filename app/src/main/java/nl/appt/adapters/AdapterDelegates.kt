package nl.appt.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import nl.appt.R
import nl.appt.model.*

/**
 * Created by Jan Jaap de Groot on 03/11/2020
 * Copyright 2020 Stichting Appt
 */

fun headerAdapterDelegate() = adapterDelegate<String, Any>(R.layout.view_header) {
    val header: TextView = findViewById(R.id.headerView)

    bind {
        header.text = item

        ViewCompat.setAccessibilityDelegate(header, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info.isHeading = true
            }
        })
    }
}

inline fun <reified T: Item> itemAdapterDelegate(crossinline callback : (T) -> Unit) = adapterDelegate<T, Any>(R.layout.view_item) {
    val textView: TextView = findViewById(R.id.textView)

    itemView.setOnClickListener {
        callback(item)
    }

    bind {
        textView.text = item.title()
    }
}

fun taxonomyAdapterDelegate(callback: (Taxonomy) -> Unit) = adapterDelegate<Taxonomy, Any>(R.layout.view_checkbox) {
    val checkBox: CheckBox = findViewById(R.id.checkBox)

    checkBox.setOnCheckedChangeListener { _, checked ->
        item.selected = checked
        callback(item)
    }

    bind {
        checkBox.text = item.title()
        checkBox.isChecked = item.selected
    }
}