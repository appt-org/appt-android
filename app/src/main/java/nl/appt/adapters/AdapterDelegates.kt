package nl.appt.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import nl.appt.R
import nl.appt.accessibility.view.accessibility
import nl.appt.databinding.ViewBlockBinding
import nl.appt.databinding.ViewHeaderDescriptionBinding
import nl.appt.databinding.ViewHomeDescriptionBinding
import nl.appt.databinding.ViewMeerItemBinding
import nl.appt.extensions.setVisible
import nl.appt.model.Block
import nl.appt.model.HomeAppLinkModel
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel
import nl.appt.model.Item
import nl.appt.model.Meer
import nl.appt.model.Taxonomy
import nl.appt.model.Training

/**
 * Created by Jan Jaap de Groot on 03/11/2020
 * Copyright 2020 Stichting Appt
 */

fun headerAdapterDelegate() = adapterDelegate<String, Any>(R.layout.view_header) {
    val header: TextView = findViewById(R.id.headerView)

    bind {
        header.text = item

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

inline fun <reified T : Meer> moreItemAdapterDelegate(crossinline callback: (T) -> Unit) =
    adapterDelegate<T, Any>(R.layout.view_meer_item) {
        val textView: TextView = findViewById(R.id.textView)
        val imageView: ImageView = findViewById(R.id.meerImageView)

        itemView.setOnClickListener {
            callback(item)
        }

        bind {
            textView.text = item.title(context)
            imageView.setImageDrawable(item.image(context))
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
                itemView.accessibility.label = getString(R.string.adgerong_accessibility_label, title)
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
            binding.blockTitle.text = item.title
            GlideApp.with(itemView.context).load(item.image)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(binding.blockImage)
        }
    }

fun listItemAdapterDelegate(itemClickedListener: (Block) -> Unit) =
    adapterDelegateViewBinding<Block, Any, ViewMeerItemBinding>(
        { layoutInflater, root -> ViewMeerItemBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.textView.text = item.title
            GlideApp.with(itemView.context).load(item.image)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(binding.meerImageView)
        }
    }

fun descriptionAdapterDelegate() =
    adapterDelegateViewBinding<String, Any, ViewHeaderDescriptionBinding>(
        { layoutInflater, root ->
            ViewHeaderDescriptionBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {

        bind {
            binding.title.text = item
        }
    }

fun homeDescriptionAdapterDelegate() =
    adapterDelegateViewBinding<Int, Any, ViewHomeDescriptionBinding>(
        { layoutInflater, root ->
            ViewHomeDescriptionBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {

        bind {
            binding.title.text = getString(item)
        }
    }

fun homeLinkAdapterDelegate(itemClickedListener: (HomeLinkModel) -> Unit) =
    adapterDelegateViewBinding<HomeLinkModel, Any, ViewBlockBinding>(
        { layoutInflater, root -> ViewBlockBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
        }
    }

fun homeAppLinkAdapterDelegate(itemClickedListener: (HomeAppLinkModel) -> Unit) =
    adapterDelegateViewBinding<HomeAppLinkModel, Any, ViewBlockBinding>(
        { layoutInflater, root -> ViewBlockBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
        }
    }

fun homePagerAdapterDelegate(itemClickedListener: (HomePagerModel) -> Unit) =
    adapterDelegateViewBinding<HomePagerModel, Any, ViewBlockBinding>(
        { layoutInflater, root -> ViewBlockBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
        }
    }

fun homeTrainingAdapterDelegate(itemClickedListener: (HomeTrainingModel) -> Unit) =
    adapterDelegateViewBinding<HomeTrainingModel, Any, ViewBlockBinding>(
        { layoutInflater, root -> ViewBlockBinding.inflate(layoutInflater, root, false) }
    ) {

        itemView.setOnClickListener {
            itemClickedListener(item)
        }

        bind {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
        }
    }