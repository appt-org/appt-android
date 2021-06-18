package nl.appt.helpers

import androidx.viewpager.widget.ViewPager
import nl.appt.adapters.ToolbarPagerAdapter

class OnPageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Preferences.setInt(ToolbarPagerAdapter.TAB_KEY, position)
    }

    override fun onPageSelected(position: Int) {
        Preferences.setInt(ToolbarPagerAdapter.TAB_KEY, position)
    }

    override fun onPageScrollStateChanged(state: Int) {}
}