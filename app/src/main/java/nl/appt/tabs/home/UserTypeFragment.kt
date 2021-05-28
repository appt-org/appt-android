package nl.appt.tabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.adapters.HomeBlocksAdapter
import nl.appt.adapters.OnBlockListener
import nl.appt.adapters.ToolbarPagerAdapter
import nl.appt.databinding.FragmentUserTypeBinding
import nl.appt.extensions.openWebsite
import nl.appt.tabs.training.TrainingActivity
import nl.appt.widgets.BaseFragment

class UserTypeFragment() : BaseFragment(), OnBlockListener {

    companion object {
        const val COLUMNS_NUMBER = 2
        private const val ARG_PARAM = "user_type"

        @JvmStatic
        fun newInstance(userType: String) =
            UserTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, userType)
                }
            }
    }

    private var userType: String? = null

    private var _binding: FragmentUserTypeBinding? = null

    private val binding get() = _binding!!

    override fun getLayoutId() = R.layout.fragment_user_type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            userType = it.getString(ARG_PARAM)
        }
        when (userType) {
            ToolbarPagerAdapter.TAB_USER_TITLE -> {
                binding.title.text = getString(R.string.home_user_title)
                setBlockAdapter(UserBlocksManager.userBlocksData)
            }
            ToolbarPagerAdapter.TAB_PROFESSIONAL_TITLE -> {
                binding.title.text = getString(R.string.home_professional_title)
                setBlockAdapter(UserBlocksManager.professionalBlocksData)
            }
        }
    }

    private fun setBlockAdapter(data: ArrayList<Any>) {
        val recyclerView = binding.blocksContainer
        recyclerView.layoutManager = GridLayoutManager(requireContext(), COLUMNS_NUMBER)
        val adapter = HomeBlocksAdapter(data, this)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLinkBlockClicked(link: String) {
        context?.openWebsite(link)
    }

    override fun onPagerBlockClicked(number: Int) {
        (activity as MainActivity).setPagerItem(number)
    }

    override fun onTrainingBlockClicked() {
        startActivity<TrainingActivity>()
    }
}
