package nl.appt.helpers

import androidx.recyclerview.widget.GridLayoutManager

object GridLayoutPortraitSpanSize : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (position == GridLayoutConst.HEADER_POSITION) {
            GridLayoutConst.PORTRAIT_SPAN_COUNT
        } else GridLayoutConst.DEFAULT_SPAN_SIZE
    }
}

object GridLayoutLandscapeSpanSize : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (position == GridLayoutConst.HEADER_POSITION) {
            GridLayoutConst.LANDSCAPE_SPAN_COUNT
        } else GridLayoutConst.DEFAULT_SPAN_SIZE
    }
}