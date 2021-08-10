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

    override fun getToolbarTitle() = getString(R.string.title_talkback_gestures)

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
            "Tikken met één vinger",
            Gesture.ONE_FINGER_TOUCH,
            Gesture.ONE_FINGER_DOUBLE_TAP,
            Gesture.ONE_FINGER_DOUBLE_TAP_HOLD,
            "Tikken met twee vingers",
            Gesture.TWO_FINGER_TAP,
            Gesture.TWO_FINGER_DOUBLE_TAP,
            Gesture.TWO_FINGER_DOUBLE_TAP_HOLD,
            Gesture.TWO_FINGER_TRIPLE_TAP,
            "Tikken met drie vingers",
            Gesture.THREE_FINGER_TAP,
            Gesture.THREE_FINGER_DOUBLE_TAP,
            Gesture.THREE_FINGER_DOUBLE_TAP_HOLD,
            Gesture.THREE_FINGER_TRIPLE_TAP,
            "Tikken met vier vingers",
            Gesture.FOUR_FINGER_TAP,
            Gesture.FOUR_FINGER_DOUBLE_TAP,
            Gesture.FOUR_FINGER_DOUBLE_TAP_HOLD,
            "Vegen met één vinger",
            Gesture.ONE_FINGER_SWIPE_UP,
            Gesture.ONE_FINGER_SWIPE_RIGHT,
            Gesture.ONE_FINGER_SWIPE_DOWN,
            Gesture.ONE_FINGER_SWIPE_LEFT,
            "Vegen met twee vingers",
            Gesture.TWO_FINGER_SWIPE_UP,
            Gesture.TWO_FINGER_SWIPE_RIGHT,
            Gesture.TWO_FINGER_SWIPE_DOWN,
            Gesture.TWO_FINGER_SWIPE_LEFT,
            "Vegen met drie vingers",
            Gesture.THREE_FINGER_SWIPE_DOWN,
            Gesture.THREE_FINGER_SWIPE_UP,
            "Verplaatsen",
            Gesture.ONE_FINGER_SWIPE_UP_THEN_DOWN,
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_UP,
            Gesture.ONE_FINGER_SWIPE_RIGHT_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_RIGHT,
            "Snelkoppelingen",
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_UP_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_UP,
            Gesture.ONE_FINGER_SWIPE_RIGHT_THEN_DOWN ,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_DOWN,
            Gesture.ONE_FINGER_SWIPE_UP_THEN_RIGHT,
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_RIGHT
        )
    }
}