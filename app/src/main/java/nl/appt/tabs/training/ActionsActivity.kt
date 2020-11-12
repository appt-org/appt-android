package nl.appt.tabs.training

import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.model.Action
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 12/11/2020
 * Copyright 2020 Stichting Appt
 */
class ActionsActivity: ToolbarActivity() {

    override fun getLayoutId() = R.layout.view_list

    override fun getToolbarTitle() = getString(R.string.title_actions)

    override fun onViewCreated() {
        super.onViewCreated()

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            itemAdapterDelegate<Action> { action ->
                // TODO
            }
        )
        adapter.items = gestures

        recyclerView.adapter  = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    companion object {
        private val gestures = listOf(
            "Navigeren",
            Action.HEADINGS,
            Action.LINKS,
            "Bewerken",
            Action.SELECTION,
            Action.COPY,
            Action.PASTE
        )
    }
}