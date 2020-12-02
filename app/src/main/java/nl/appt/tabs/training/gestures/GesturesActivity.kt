package nl.appt.tabs.training.gestures

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.trainingAdapterDelegate
import nl.appt.extensions.setGesture
import nl.appt.extensions.setGestures
import nl.appt.model.Gesture
import nl.appt.services.ApptService
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
            trainingAdapterDelegate<Gesture> { gesture ->
                onGestureClicked(gesture)
            }
        )
        adapter.items = gestures

        recyclerView.adapter  = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.practice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_practice) {
            onPracticeClicked()
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun needApptService(): Boolean {
        if (Accessibility.isTalkBackEnabled(this)) {
            if (!ApptService.isEnabled(this)) {
                ApptService.enable(this)
                return true
            }
        }
        return false
    }

    private fun onGestureClicked(gesture: Gesture) {
        if (needApptService()) {
            return
        }

        startActivity<GestureActivity>(REQUEST_CODE_SINGLE) {
            setGesture(gesture)
        }
    }

    private fun onPracticeClicked() {
        if (needApptService()) {
            return
        }

        startActivity<GestureActivity>(REQUEST_CODE_MULTIPLE) {
            setGestures(Gesture.all())
        }
    }

    companion object {
        private const val REQUEST_CODE_SINGLE = 1
        private const val REQUEST_CODE_MULTIPLE = 2

        private val gestures = arrayListOf(
            "Selecteren",
            Gesture.TOUCH,
            Gesture.DOUBLE_TAP,
            "Navigeren",
            Gesture.SWIPE_RIGHT,
            Gesture.SWIPE_LEFT,
            Gesture.SWIPE_DOWN,
            Gesture.SWIPE_UP,
            "Scrollen",
            Gesture.SCROLL_DOWN,
            Gesture.SCROLL_UP,
            Gesture.SCROLL_LEFT,
            Gesture.SCROLL_RIGHT,
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