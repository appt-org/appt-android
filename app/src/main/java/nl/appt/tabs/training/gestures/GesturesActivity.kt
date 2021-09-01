package nl.appt.tabs.training.gestures

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.trainingAdapterDelegate
import nl.appt.extensions.*
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
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun onGestureClicked(gesture: Gesture) {
        if (Accessibility.isTalkBackEnabled(this)) {
            showError(R.string.service_reason_message)
            return
        }

        startActivity<GestureActivity>(REQUEST_CODE_SINGLE) {
            setGesture(gesture)
        }
    }

    private fun onPracticeClicked() {
        AlertDialog.Builder(this)
            .setMessage("Wil je oefenen met of zonder instructies?")
            .setPositiveButton("Met instructies") { _, _ ->
                startPractice(true)
            }
            .setNegativeButton("Zonder instructies") { _, _ ->
                startPractice(false)
            }
            .show()
    }

    private fun startPractice(instructions: Boolean) {
        if (Accessibility.isTalkBackEnabled(this)) {
            if (!ApptService.isEnabled(this)) {
                ApptService.enable(this, instructions)
                return
            }
        }

        val gestures = Gesture.randomized()
        gestures.forEach { gesture ->
            gesture.completed(this, false)
        }

        startActivity<GestureActivity>(REQUEST_CODE_MULTIPLE) {
            setGestures(gestures)
            setInstructions(instructions)
        }
    }

    companion object {
        private const val REQUEST_CODE_SINGLE = 1
        private const val REQUEST_CODE_MULTIPLE = 2

        private val gestures = arrayListOf(
            "Tikken met 1 vinger",
            Gesture.ONE_FINGER_TOUCH,
            Gesture.ONE_FINGER_DOUBLE_TAP,
            Gesture.ONE_FINGER_DOUBLE_TAP_HOLD,
            "Tikken met 2 vingers",
            Gesture.TWO_FINGER_TAP,
            Gesture.TWO_FINGER_DOUBLE_TAP,
            Gesture.TWO_FINGER_DOUBLE_TAP_HOLD,
            Gesture.TWO_FINGER_TRIPLE_TAP,
            "Tikken met 3 vingers",
            Gesture.THREE_FINGER_TAP,
            Gesture.THREE_FINGER_DOUBLE_TAP,
            Gesture.THREE_FINGER_DOUBLE_TAP_HOLD,
            Gesture.THREE_FINGER_TRIPLE_TAP,
            "Tikken met 4 vingers",
            Gesture.FOUR_FINGER_TAP,
            Gesture.FOUR_FINGER_DOUBLE_TAP,
            Gesture.FOUR_FINGER_DOUBLE_TAP_HOLD,
            "Vegen met 1 vinger",
            Gesture.ONE_FINGER_SWIPE_RIGHT,
            Gesture.ONE_FINGER_SWIPE_LEFT,
            Gesture.ONE_FINGER_SWIPE_UP,
            Gesture.ONE_FINGER_SWIPE_DOWN,
            "Vegen met 2 vingers",
            Gesture.TWO_FINGER_SWIPE_UP,
            Gesture.TWO_FINGER_SWIPE_DOWN,
            Gesture.TWO_FINGER_SWIPE_RIGHT,
            Gesture.TWO_FINGER_SWIPE_LEFT,
            "Vegen met 3 vingers",
            Gesture.THREE_FINGER_SWIPE_UP,
            Gesture.THREE_FINGER_SWIPE_DOWN,
            "Verplaatsing",
            Gesture.ONE_FINGER_SWIPE_UP_THEN_DOWN,
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_UP,
            Gesture.ONE_FINGER_SWIPE_RIGHT_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_RIGHT,
            "Snelkoppelingen",
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_UP_THEN_LEFT,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_UP,
            Gesture.ONE_FINGER_SWIPE_RIGHT_THEN_DOWN,
            Gesture.ONE_FINGER_SWIPE_LEFT_THEN_DOWN,
            Gesture.ONE_FINGER_SWIPE_UP_THEN_RIGHT,
            Gesture.ONE_FINGER_SWIPE_DOWN_THEN_RIGHT,
        )
    }
}