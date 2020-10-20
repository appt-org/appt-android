package nl.appt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_header.view.*
import kotlinx.android.synthetic.main.view_item.view.*
import nl.appt.R
import nl.appt.model.Header
import nl.appt.model.Item



class ItemRecyclerViewAdapter<T: Item>(
    private val items: List<Any>,
    private val listener: Callback<T>?
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    interface Callback<T> {
        fun onItemClicked(item: T)
    }

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            if (v.tag is Item) {
                (v.tag as? T)?.let { item ->
                    listener?.onItemClicked(item)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]

        return if (item is Header) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.view_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.view_item, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (item is Header && holder is ItemRecyclerViewAdapter<*>.HeaderViewHolder) {
            holder.header.text = item.title

            ViewCompat.setAccessibilityDelegate(holder.header, object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    info.isHeading = true
                }
            })
        } else if (item is Item && holder is ItemRecyclerViewAdapter<*>.ItemViewHolder) {
            holder.title.text = item.title

            with(holder.view) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    open class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    inner class HeaderViewHolder(view: View) : ViewHolder(view) {
        val header: TextView = view.headerView
    }

    inner class ItemViewHolder(view: View) : ViewHolder(view) {
        val title: TextView = view.textView
    }
}
