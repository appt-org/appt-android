package nl.appt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import nl.appt.tabs.information.InformationFragment
import nl.appt.tabs.knowledge.KnowledgeFragment
import nl.appt.tabs.training.TrainingFragment
import nl.appt.widgets.BaseActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class MainActivity : BaseActivity() {

    private val tabs = listOf(R.id.tab_training, R.id.tab_knowledge, R.id.tab_information)
    private val fragments = listOf(TrainingFragment(), KnowledgeFragment(), InformationFragment())

    override fun getViewId(): Int {
        return R.layout.activity_main
    }

    override fun onViewCreated() {
        this.title = ""

        // Tab adapter
        val tabAdapter = TabPagerAdapter(supportFragmentManager, fragments)
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = tabAdapter
        viewPager.addOnPageChangeListener(onPageChangeListener)

        // Navigation view
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigationView.selectedItemId = tabs[0]
    }

    class TabPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            // Ignored
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // Ignored
        }

        override fun onPageSelected(position: Int) {
            navigationView.menu.getItem(position).isChecked = true
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val index = tabs.indexOf(item.itemId)

        fragments[index].willShow()
        viewPager.currentItem = index

        when (item.itemId) {
            R.id.tab_training, R.id.tab_knowledge, R.id.tab_information -> {
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }
}