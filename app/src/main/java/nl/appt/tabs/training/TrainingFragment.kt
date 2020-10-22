package nl.appt.tabs.training

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_training.view.*
import nl.appt.R
import nl.appt.adapters.ItemRecyclerViewAdapter
import nl.appt.widgets.ToolbarFragment
import nl.appt.extensions.startActivity
import nl.appt.model.Gesture
import nl.appt.model.Header

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingFragment: ToolbarFragment(), ItemRecyclerViewAdapter.Callback<Gesture> {

    private val gestures = listOf(
        Header("Selecteren"),
        Gesture.TOUCH,
        Gesture.DOUBLE_TAP,
        Header("Navigeren"),
        Gesture.SWIPE_RIGHT,
        Gesture.SWIPE_LEFT,
        Gesture.SWIPE_UP,
        Gesture.SWIPE_DOWN,
        Header("Verplaatsen"),
        Gesture.SWIPE_UP_DOWN,
        Gesture.SWIPE_DOWN_UP,
        Gesture.SWIPE_RIGHT_LEFT,
        Gesture.SWIPE_LEFT_RIGHT,
        Header("Snelkoppelingen"),
        Gesture.SWIPE_DOWN_LEFT,
        Gesture.SWIPE_UP_LEFT,
        Gesture.SWIPE_LEFT_UP,
        Gesture.SWIPE_RIGHT_DOWN ,
        Gesture.SWIPE_LEFT_DOWN,
        Gesture.SWIPE_UP_RIGHT,
        Gesture.SWIPE_DOWN_RIGHT
    )

    override fun getViewId(): Int {
        return R.layout.fragment_training
    }

    override fun getTitle(): String {
        return getString(R.string.title_training)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            context?.startActivity<TrainingActivity>()
        }

        with(view.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemRecyclerViewAdapter(gestures, this@TrainingFragment)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onItemClicked(item: Gesture) {
        startActivity<TrainingActivity> {
            putExtra("gesture", item)
        }
    }
}