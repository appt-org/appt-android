package nl.appt.tabs.information

import nl.appt.R
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class InformationFragment : ToolbarFragment() {

    override fun getViewId(): Int {
        return R.layout.fragment_information
    }

    override fun getTitle(): String {
        return getString(R.string.title_information)
    }
}
