package nl.appt.tabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import nl.appt.R
import nl.appt.adapters.ToolbarPagerAdapter
import nl.appt.databinding.FragmentHomeBinding
import nl.appt.widgets.BaseFragment

class HomeFragment() : BaseFragment() {

    private val toolbarPagerAdapter by lazy {
        ToolbarPagerAdapter(childFragmentManager)
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun getLayoutId() = R.layout.fragment_home

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = toolbarPagerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
