package nl.appt.adapters

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import nl.appt.R
import nl.appt.accessibility.view.accessibility
import nl.appt.databinding.*
import nl.appt.extensions.load
import nl.appt.extensions.setVisible
import nl.appt.model.*

/**
 * Created by Jan Jaap de Groot on 03/11/2020
 * Copyright 2020 Stichting Appt
 */

fun headerAdapterDelegate() = adapterDelegate<String, Any>(R.layout.view_header) {
    val header: TextView = findViewById(R.id.headerView)

    bind {
        header.text = item

        setAccessibilityHeading(header)
    }
}

inline fun <reified T : Item> itemAdapterDelegate(crossinline callback: (T) -> Unit) =
    adapterDelegate<T, Any>(R.layout.view_item) {
        val textView: TextView = findViewById(R.id.textView)

        itemView.setOnClickListener {
            callback(item)
        }

        bind {
            textView.text = item.title(context)
        }
    }

inline fun <reified T : Training> trainingAdapterDelegate(crossinline callback: (T) -> Unit) =
    adapterDelegate<T, Any>(R.layout.view_training) {
        val textView: TextView = findViewById(R.id.textView)
        val imageView: ImageView = findViewById(R.id.imageView)

        itemView.setOnClickListener {
            callback(item)
        }

        bind {
            val title = item.title(context)
            val completed = item.completed(context)

            textView.text = title

            if (completed) {
                imageView.setVisible(true)
                itemView.accessibility.label =
                    getString(R.string.adgerong_accessibility_label, title)
            } else {
                imageView.setVisible(false)
                itemView.accessibility.label = title
            }
        }
    }

fun taxonomyAdapterDelegate(callback: (Taxonomy) -> Unit) =
    adapterDelegate<Taxonomy, Any>(R.layout.view_checkbox) {
        val checkBox: CheckBox = findViewById(R.id.checkBox)

        checkBox.setOnCheckedChangeListener { _, checked ->
            item.selected = checked
            callback(item)
        }

        bind {
            checkBox.text = item.title(context)
            checkBox.isChecked = item.selected
        }
    }

fun blockAdapterDelegate(itemClickedListener: (Block) -> Unit) =
    adapterDelegateViewBinding<Block, Any, ViewBlockBinding>(
        { layoutInflater, root -> ViewBlockBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.textView.text = item.title

            Log.d("AdapterDelegates", "Loading url: ${item.image}")

            binding.imageView.load(item.image)

            itemView.accessibility.label = item.title
            setAccessibilityButtonDelegate(itemView)
        }
    }

fun listItemAdapterDelegate(itemClickedListener: (Block) -> Unit) =
    adapterDelegateViewBinding<Block, Any, ViewIconItemBinding>(
        { layoutInflater, root -> ViewIconItemBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.textView.text = item.title

            binding.imageView.load(item.image)

            itemView.accessibility.label = item.title
            setAccessibilityButtonDelegate(itemView)
        }
    }

fun textAdapterDelegate() =
    adapterDelegateViewBinding<String, Any, ViewTextBinding>(
        { layoutInflater, root ->
            ViewTextBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {

        bind {
            binding.textView.text = item

            setAccessibilityHeading(itemView)
        }
    }


private fun setAccessibilityButtonDelegate(view: View) {
    ViewCompat.setAccessibilityDelegate(view, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfoCompat
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.className = Button::class.java.name
        }
    })
}

private fun setAccessibilityHeading(header: View){
    ViewCompat.setAccessibilityDelegate(header, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfoCompat
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.isHeading = true
        }
    })
}