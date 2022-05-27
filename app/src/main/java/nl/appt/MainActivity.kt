package nl.appt

import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.extensions.setVisible
import nl.appt.helpers.Events
import nl.appt.helpers.Preferences
import nl.appt.widgets.WebActivity

/**
 * Created by Jan Jaap de Groot on 27/05/2022
 * Copyright 2022 Stichting Appt
 */
class MainActivity : WebActivity() {

    override fun onViewCreated() {
        super.onViewCreated()

        this.title = getString(R.string.app_name)
        toolbar?.setVisible(false)

        events.property(Events.Property.talkback, Accessibility.isTalkBackEnabled(this))

        Preferences.getUrl(this)?.let { url ->
            load(url)
        } ?: run {
            load(getString(R.string.appt_url))
        }
    }
}