package nl.appt.tabs.training

import android.os.Bundle
import android.view.View
import nl.appt.R
import nl.appt.widgets.ToolbarFragment
import nl.appt.extensions.startActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingFragment : ToolbarFragment() {

    override fun getViewId(): Int {
        return R.layout.fragment_training
    }

    override fun getTitle(): String {
        return getString(R.string.title_training)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener {
            context?.startActivity<TrainingActivity>()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}