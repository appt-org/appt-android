package nl.appt.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import nl.appt.R
import nl.appt.tabs.home.UserTypeFragment

private const val TABS_COUNT = 2

class ToolbarPagerAdapter(fm: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabUserTitle = context.getString(R.string.home_tab_user_title)
    private val tabProfessionalTitle = context.getString(R.string.home_tab_professional_title)

    override fun getCount() = TABS_COUNT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserTypeFragment.newInstance(tabUserTitle)
            }
            else -> {
                UserTypeFragment.newInstance(tabProfessionalTitle)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> tabUserTitle
            else -> tabProfessionalTitle
        }
    }
}