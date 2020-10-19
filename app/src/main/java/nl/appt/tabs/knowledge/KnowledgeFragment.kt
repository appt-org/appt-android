package nl.appt.tabs.knowledge

import nl.appt.R
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment : ToolbarFragment() {

    override fun getViewId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun getTitle(): String {
        return getString(R.string.title_knowledge)
    }
}
