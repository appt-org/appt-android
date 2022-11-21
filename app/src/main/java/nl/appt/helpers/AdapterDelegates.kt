package nl.appt.helpers

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.google.android.material.slider.Slider
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import nl.appt.R
import nl.appt.database.Page
import nl.appt.model.Item
import nl.appt.model.Header
import nl.appt.model.Range

/**
 * Created by Jan Jaap de Groot on 01/07/2022
 * Copyright 2022 Stichting Appt
 */

fun headerAdapterDelegate(callback: ((Header) -> Unit)? = null) =
    adapterDelegate<Header, Any>(R.layout.view_header) {
        val header: TextView = findViewById(R.id.headerView)

        bind {
            ViewCompat.setAccessibilityHeading(header, true)
            header.text = item.text

            header.setOnClickListener {
                callback?.let {
                    it(item)
                }
            }
        }
    }

fun textAdapterDelegate(callback: (String) -> Unit) =
    adapterDelegate<String, Any>(R.layout.view_text) {
        val textView: TextView = findViewById(R.id.textView)

        bind {
            textView.text = item

            textView.setOnClickListener {
                callback(item)
            }
        }
    }

fun itemAdapterDelegate(callback: (Item) -> Unit) =
    adapterDelegate<Item, Any>(R.layout.view_item) {
        val imageView: ImageView = findViewById(R.id.imageView)
        val titleView: TextView = findViewById(R.id.titleView)

        bind {
            imageView.setImageResource(item.icon)
            titleView.setText(item.title)
            itemView.contentDescription = titleView.text
            Accessibility.setButton(itemView, true)

            itemView.setOnClickListener {
                callback(item)
            }
        }
    }

fun sliderAdapterDelegate(multiplier: Int = 100, callback: (Float) -> Unit) =
    adapterDelegate<Range, Any>(R.layout.view_slider) {
        val title: TextView = findViewById(R.id.titleView)
        val slider: Slider = findViewById(R.id.slider)

        bind {
            title.text = item.text

            slider.setLabelFormatter { value: Float ->
                val number = (value * multiplier).toInt()
                String.format("%d%%", number)
            }

            slider.addOnChangeListener { _, value, fromUser ->
                val number = (value * multiplier).toInt()
                title.text = String.format("%s %d%%", item.text, number)

                if (fromUser) {
                    callback(value)
                }
            }

            slider.valueFrom = item.minimum
            slider.valueTo = item.maximum
            slider.stepSize = item.step
            slider.value = item.value
        }
    }

inline fun <reified T: Page> pageAdapterDelegate(
    crossinline onClick: (T) -> Unit,
    crossinline onLongClick: (T) -> Unit
) = adapterDelegate<T, Any>(R.layout.view_page) {
    val titleView: TextView = findViewById(R.id.titleView)
    val descriptionView: TextView = findViewById(R.id.descriptionView)

    bind {
        titleView.text = item.title ?: getString(R.string.unknown)
        descriptionView.text = item.url
        itemView.contentDescription = String.format("%s, %s", titleView.text, descriptionView.text)
        Accessibility.setButton(itemView, true)

        itemView.setOnClickListener {
            onClick(item)
        }

        itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }
}