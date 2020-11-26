package nl.appt.tabs.training

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.extensions.setText
import nl.appt.extensions.setTitle
import nl.appt.model.Course
import nl.appt.tabs.training.actions.ActionsActivity
import nl.appt.tabs.training.gestures.GesturesActivity
import nl.appt.widgets.ToolbarFragment
import nl.appt.widgets.TextActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingFragment: ToolbarFragment() {

    override fun getLayoutId() = R.layout.view_list

    override fun getTitle()= getString(R.string.title_training)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            itemAdapterDelegate<Course> { course ->
                when (course) {
                    Course.TALKBACK_GESTURES -> {
                        startActivity<GesturesActivity>()
                    }
                    Course.TALKBACK_ENABLE -> {
                        startActivity<TextActivity> {
                            setTitle(getString(R.string.talkback_enable_title))
                            setText(getString(R.string.talkback_enable_text))
                        }
                    }
                    Course.TALKBACK_ACTIONS -> {
                        startActivity<ActionsActivity>()
                    }
                }
            }
        )
        adapter.items = gestures

        view.recyclerView.adapter  = adapter
        view.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    companion object {
        private val gestures = listOf(
            "TalkBack",
            Course.TALKBACK_GESTURES,
            Course.TALKBACK_ENABLE,
            Course.TALKBACK_ACTIONS
        )
    }
}