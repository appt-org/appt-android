package nl.appt.tabs.training

import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.extensions.setGesture
import nl.appt.model.Gesture
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class GesturesActivity: ToolbarActivity() {

    override fun getLayoutId() = R.layout.view_list

    override fun getToolbarTitle() = getString(R.string.title_gestures)

    override fun onViewCreated() {
        super.onViewCreated()

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            itemAdapterDelegate<Gesture> { gesture ->
                startActivity<GestureActivity> {
                    setGesture(gesture)
                }
            }
        )
        adapter.items = gestures

        recyclerView.adapter  = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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