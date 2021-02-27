package nl.appt.extensions

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.appt.R

/**
 * Created by Jan Jaap de Groot on 05/11/2020
 * Copyright 2020 Stichting Appt
 */

fun RecyclerView.onInfiniteScroll(callback: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                    adapter?.itemCount?.let { total ->
                        if ((layoutManager.findFirstCompletelyVisibleItemPosition() + layoutManager.childCount) >= total) {
                            callback()
                        }
                    }
                }
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    })
}

fun RecyclerView.addItemDecoration(direction: Int = DividerItemDecoration.VERTICAL, resource: Int = R.drawable.divider) {
    ContextCompat.getDrawable(context, resource)?.let { drawable ->
        val decoration = DividerItemDecoration(context, direction)
        decoration.setDrawable(drawable)

        addItemDecoration(decoration)
    }
}