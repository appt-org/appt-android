package nl.appt.tabs.services

import nl.appt.R
import nl.appt.widgets.ToolbarFragment

class ServicesFragment : ToolbarFragment() {

    override fun getTitle() = getString(R.string.diensten_toolbar_title)

    override fun getLayoutId() = R.layout.fragment_services
}