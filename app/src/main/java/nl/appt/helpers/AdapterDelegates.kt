package nl.appt.helpers

import android.widget.TextView
import androidx.core.view.ViewCompat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import nl.appt.R
import nl.appt.database.Bookmark
import nl.appt.model.Action
import nl.appt.model.Header

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

fun actionAdapterDelegate(callback: (Action) -> Unit) =
    adapterDelegate<Action, Any>(R.layout.view_text) {
        val textView: TextView = findViewById(R.id.textView)

        bind {
            textView.setText(item.string)

            textView.setOnClickListener {
                callback(item)
            }
        }
    }

fun bookmarkAdapterDelegate(onClick: (Bookmark) -> Unit, onLongClick: (Bookmark) -> Unit) =
    adapterDelegate<Bookmark, Any>(R.layout.view_page) {
        val titleView: TextView = findViewById(R.id.titleView)
        val descriptionView: TextView = findViewById(R.id.descriptionView)

        bind {
            titleView.text = item.title ?: getString(R.string.unknown)
            descriptionView.text = item.url

            itemView.setOnClickListener {
                onClick(item)
            }

            itemView.setOnLongClickListener {
                onLongClick(item)
                true
            }
        }
    }