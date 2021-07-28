package nl.appt.tabs.training.actions

import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.trainingAdapterDelegate
import nl.appt.extensions.setAction2
import nl.appt.model.Action
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 12/11/2020
 * Copyright 2020 Stichting Appt
 */
class ActionsActivity: ToolbarActivity() {

    override fun getLayoutId() = R.layout.view_list

    override fun getToolbarTitle() = getString(R.string.title_talkback_actions)

    override fun onViewCreated() {
        super.onViewCreated()

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            trainingAdapterDelegate<Action> { action ->
                startActivity<ActionActivity>(REQUEST_CODE) {
                    setAction2(action)
                }
            }
        )
        adapter.items = gestures

        recyclerView.adapter  = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        private val REQUEST_CODE = 1

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