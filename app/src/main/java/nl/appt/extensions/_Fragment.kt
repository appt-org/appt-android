package nl.appt.extensions

import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import nl.appt.R
import nl.appt.helpers.Accessibility

var Fragment.isLoading: Boolean
    get() {
        return view?.findViewById<ProgressBar>(R.id.progressBar)?.visible ?: false
    }
    set(value) {
        if (value) {
            Accessibility.announce(requireContext(), getString(R.string.loading))
        }
        view?.findViewById<ProgressBar>(R.id.progressBar)?.visible = value
    }