package nl.appt.tabs.training

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.gestureAdapterDelegate
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.widgets.ToolbarFragment
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingFragment: ToolbarFragment() {

    override fun getLayoutId() = R.layout.view_list

    override fun getTitle(): String? {
        return getString(R.string.title_training)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            gestureAdapterDelegate { gesture ->
                startActivity<TrainingActivity> {
                    putExtra("gesture", gesture)
                }
            }
        )
        adapter.items = gestures

        view.recyclerView.adapter  = adapter
        view.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    companion object {
        private val gestures = listOf(
            "Selecteren",
            Gesture.TOUCH,
            Gesture.DOUBLE_TAP,
            "Navigeren",
            Gesture.SWIPE_RIGHT,
            Gesture.SWIPE_LEFT,
            Gesture.SWIPE_UP,
            Gesture.SWIPE_DOWN,
            "Verplaatsen",
            Gesture.SWIPE_UP_DOWN,
            Gesture.SWIPE_DOWN_UP,
            Gesture.SWIPE_RIGHT_LEFT,
            Gesture.SWIPE_LEFT_RIGHT,
            "Snelkoppelingen",
            Gesture.SWIPE_DOWN_LEFT,
            Gesture.SWIPE_UP_LEFT,
            Gesture.SWIPE_LEFT_UP,
            Gesture.SWIPE_RIGHT_DOWN ,
            Gesture.SWIPE_LEFT_DOWN,
            Gesture.SWIPE_UP_RIGHT,
            Gesture.SWIPE_DOWN_RIGHT
        )
    }
}