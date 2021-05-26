package nl.appt.tabs.more

import nl.appt.R
import nl.appt.widgets.ToolbarActivity

class ProfileActivity : ToolbarActivity() {

    override fun getToolbarTitle() = getString(R.string.topic_profile_title)

    override fun getLayoutId() = R.layout.activity_profile

}