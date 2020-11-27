package nl.appt.tabs.training.actions

import android.content.Intent
import kotlinx.android.synthetic.main.activity_action.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.extensions.getAction2
import nl.appt.extensions.showDialog
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

        if (!Accessibility.isTalkBackEnabled(this)) {
            showDialog(R.string.talkback_disabled_title, R.string.talkback_disabled_explanation) {
                finish()
            }
        }
    }

    override fun correct(action: Action) {
        action.completed(this, true)
        setResult(RESULT_OK)

        toast("Training afgerond!") {
            finish()
        }
    }

    override fun incorrect(action: Action, feedback: String) {
        toast(feedback)
    }

    override fun startActivity(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            // Ignore view actions
            return
        }
        super.startActivity(intent)
    }
}