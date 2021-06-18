package nl.appt.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import nl.appt.tabs.home.UserTypeFragment

class ToolbarPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val TABS_COUNT = 2
        const val TAB_USER_TITLE = "Gebruiker"
        const val TAB_PROFESSIONAL_TITLE = "Professional"
        const val TAB_KEY = "tabKey"
    }

    override fun getCount() = TABS_COUNT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserTypeFragment.newInstance(TAB_USER_TITLE)
            }
            else -> {
                UserTypeFragment.newInstance(TAB_PROFESSIONAL_TITLE)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> TAB_USER_TITLE
            else -> TAB_PROFESSIONAL_TITLE
        }
    }
}