package nl.appt.tabs.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.adapters.ToolbarPagerAdapter
import nl.appt.adapters.homeAppLinkAdapterDelegate
import nl.appt.adapters.homeDescriptionAdapterDelegate
import nl.appt.adapters.homeLinkAdapterDelegate
import nl.appt.adapters.homePagerAdapterDelegate
import nl.appt.adapters.homeTrainingAdapterDelegate
import nl.appt.databinding.FragmentUserTypeBinding
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setArticleType
import nl.appt.extensions.setTitle
import nl.appt.extensions.setUri
import nl.appt.helpers.GridLayoutConst
import nl.appt.helpers.GridLayoutSpanSize
import nl.appt.model.Article
import nl.appt.tabs.news.ArticleActivity
import nl.appt.tabs.training.TrainingActivity
import nl.appt.widgets.BaseFragment

private const val ARG_PARAM = "user_type"

class UserTypeFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(userType: String) =
            UserTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, userType)
                }
            }
    }

    private val adapterDelegate = ListDelegationAdapter(
        homeDescriptionAdapterDelegate(),
        homeTrainingAdapterDelegate {
            onTrainingBlockClicked()
        },
        homeAppLinkAdapterDelegate {
            onAppLinkBlockClicked(it.title, it.link)
        },
        homeLinkAdapterDelegate {
            onLinkBlockClicked(it.link)
        },
        homePagerAdapterDelegate {
            onPagerBlockClicked(it.pagerPosition)
        }
    )

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
        setAdapter()
        when (userType) {
            ToolbarPagerAdapter.TAB_USER_TITLE -> {
                adapterDelegate.items = UserBlocksManager.userBlocksData
                adapterDelegate.notifyDataSetChanged()
            }
            ToolbarPagerAdapter.TAB_PROFESSIONAL_TITLE -> {
                adapterDelegate.items = UserBlocksManager.professionalBlocksData
                adapterDelegate.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        val manager = GridLayoutManager(context, GridLayoutConst.SPAN_COUNT)
        manager.spanSizeLookup = GridLayoutSpanSize
        binding.blocksContainer.run {
            layoutManager = manager
            adapter = adapterDelegate
        }
    }

    private fun onLinkBlockClicked(link: String) {
        requireContext().openWebsite(link)
    }

    private fun onAppLinkBlockClicked(title: String, link: Uri) {
        startActivity<ArticleActivity> {
            setArticleType(Article.Type.PAGE)
            setTitle(title)
            setUri(link)
        }
    }

    private fun onPagerBlockClicked(number: Int) {
        (activity as MainActivity).setPagerItem(number)
    }

    private fun onTrainingBlockClicked() {
        startActivity<TrainingActivity>()
    }
}
