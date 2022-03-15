package nl.appt.tabs.home

import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.widgets.blocks.BlocksFragment
import nl.appt.widgets.blocks.BlocksViewModel

class HomeViewModel : BlocksViewModel("wp-content/themes/appt/home.json")

class HomeFragment : BlocksFragment() {

    override fun getTitle(): String {
        return getString(R.string.home_toolbar_title)
    }

    override fun viewModel(): BlocksViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}
