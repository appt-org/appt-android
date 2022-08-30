package nl.appt.extensions

import android.app.Activity
import android.widget.ProgressBar
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.announce

var Activity.isLoading: Boolean
    get() {
        return findViewById<ProgressBar>(R.id.progressBar)?.visible ?: false
    }
    set(value) {
        if (value) {
            Accessibility.announce(this, getString(R.string.loading))
        }

        findViewById<ProgressBar>(R.id.progressBar)?.visible = value
    }