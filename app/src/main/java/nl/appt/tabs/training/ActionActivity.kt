package nl.appt.tabs.training

import kotlinx.android.synthetic.main.activity_action.*
import nl.appt.R
import nl.appt.extensions.getAction2
import nl.appt.extensions.toast
import nl.appt.model.Action
import nl.appt.views.actions.ActionViewCallback
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 16/11/2020
 * Copyright 2020 Stichting Appt
 */
class ActionActivity: ToolbarActivity(), ActionViewCallback {

    private val action: Action by lazy {
        intent.getAction2() ?: Action.SELECTION
    }

    override fun getLayoutId() = R.layout.activity_action

    override fun getToolbarTitle() = action.title

    override fun onViewCreated() {
        super.onViewCreated()

        val view = action.view(this)
        view.callback = this
        scrollView.addView(view)
    }

    override fun correct(action: Action) {
        toast("Training afgerond!")
        finish()
    }

    override fun incorrect(action: Action, feedback: String) {
        toast(feedback)
    }
}