package nl.appt.helpers

import androidx.recyclerview.widget.GridLayoutManager

object GridLayoutSpanSize : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (position == GridLayoutConst.HEADER_POSITION) {
            GridLayoutConst.SPAN_COUNT
        } else GridLayoutConst.DEFAULT_SPAN_SIZE
    }
}