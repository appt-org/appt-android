package nl.appt.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val ERROR_INVALID_HOLDER = "Invalid view holder "
        const val ERROR_INVALID_DATA_TYPE = "Invalid type of data "
        const val ERROR_INVALID_VIEW_TYPE = "Invalid view type "

        const val IMAGE = "beeld"
    }

    abstract fun bind(item: T)
}