package nl.appt.helpers

import androidx.viewpager.widget.ViewPager

class OnPageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Preferences.setInt(PrefsKeys.TAB_KEY, position)
    }

    override fun onPageSelected(position: Int) {
        Preferences.setInt(PrefsKeys.TAB_KEY, position)
    }

    override fun onPageScrollStateChanged(state: Int) {}
}