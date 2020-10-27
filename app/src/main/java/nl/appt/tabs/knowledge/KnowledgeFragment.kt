package nl.appt.tabs.knowledge

import android.os.Bundle
import android.util.Log
import android.view.View
import nl.appt.R
import nl.appt.api.API
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment : ToolbarFragment() {

    private val TAG = "TrainingFragment"

    override fun getViewId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun getTitle(): String {
        return getString(R.string.title_knowledge)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        API.getArticles { response ->
            Log.d(TAG, "Result: ${response.result}, error: ${response.error}")
        }
    }
}
