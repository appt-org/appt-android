package nl.appt.tabs.services

import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.widgets.blocks.BlocksFragment
import nl.appt.widgets.blocks.BlocksViewModel

class ServicesViewModel : BlocksViewModel("wp-content/themes/appt/services.json")

class ServicesFragment : BlocksFragment() {

    override fun getTitle(): String {
        return getString(R.string.services_toolbar_title)
    }

    override fun viewModel(): BlocksViewModel {
        return ViewModelProvider(this).get(ServicesViewModel::class.java)
    }
}