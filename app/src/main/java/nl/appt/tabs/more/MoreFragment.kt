package nl.appt.tabs.more

import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.widgets.blocks.BlocksFragment
import nl.appt.widgets.blocks.BlocksViewModel

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */

class MoreViewModel : BlocksViewModel("wp-content/themes/appt/more.json")

class MoreFragment : BlocksFragment() {

    override fun getTitle() = getString(R.string.tab_more)

    override fun viewModel(): BlocksViewModel {
        return ViewModelProvider(this).get(MoreViewModel::class.java)
    }
}
