package nl.appt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.helpers.Events
import nl.appt.tabs.more.MoreFragment
import nl.appt.tabs.knowledge.KnowledgeFragment
import nl.appt.tabs.news.NewsFragment
import nl.appt.tabs.training.TrainingFragment
import nl.appt.widgets.BaseActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class MainActivity : BaseActivity() {

    private val tabs = listOf(R.id.tab_training, R.id.tab_knowledge, R.id.tab_news, R.id.tab_more)
    private val fragments = listOf(TrainingFragment(), KnowledgeFragment(), NewsFragment(), MoreFragment())

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onViewCreated() {
        this.title = ""
        events.property(Events.Property.talkback, Accessibility.isTalkBackEnabled(this))

        // Tab adapter
        val tabAdapter = TabPagerAdapter(supportFragmentManager, fragments)
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = tabs.size
        viewPager.adapter = tabAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // Ignored
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Ignored
            }

            override fun onPageSelected(position: Int) {
                navigationView.menu.getItem(position).isChecked = true
            }
        })

        // Navigation view
        navigationView.setOnNavigationItemSelectedListener { item ->
            val index = tabs.indexOf(item.itemId)

            fragments[index].willShow()
            viewPager.currentItem = index

            tabs.contains(item.itemId)
        }
        navigationView.selectedItemId = tabs[0]
    }

    private class TabPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}