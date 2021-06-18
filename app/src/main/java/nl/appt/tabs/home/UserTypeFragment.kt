package nl.appt.tabs.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.adapters.homeAppLinkAdapterDelegate
import nl.appt.adapters.homeDescriptionAdapterDelegate
import nl.appt.adapters.homeLinkAdapterDelegate
import nl.appt.adapters.homePagerAdapterDelegate
import nl.appt.adapters.homeTrainingAdapterDelegate
import nl.appt.databinding.FragmentUserTypeBinding
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setArticleType
import nl.appt.extensions.setUri
import nl.appt.helpers.GridLayoutConst
import nl.appt.helpers.GridLayoutLandscapeSpanSize
import nl.appt.helpers.GridLayoutPortraitSpanSize
import nl.appt.model.Article
import nl.appt.tabs.news.ArticleActivity
import nl.appt.tabs.training.TrainingActivity
import nl.appt.widgets.BaseFragment

private const val ARG_PARAM = "user_type"

class UserTypeFragment : BaseFragment() {

    companion object {
        fun newInstance(userType: String) =
            UserTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, userType)
                }
            }
    }

    private lateinit var manager: GridLayoutManager

    private val adapterDelegate = ListDelegationAdapter(
        homeDescriptionAdapterDelegate(),
        homeTrainingAdapterDelegate {
            onTrainingBlockClicked()
        },
        homeAppLinkAdapterDelegate {
            onAppLinkBlockClicked(it.linkId)
        },
        homeLinkAdapterDelegate {
            onLinkBlockClicked(it.linkId)
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
        when (userType) {
            requireContext().getString(R.string.home_tab_user_title) -> {
                adapterDelegate.items = UserBlocksManager.userBlocksData
            }
            requireContext().getString(R.string.home_tab_professional_title) -> {
                adapterDelegate.items = UserBlocksManager.professionalBlocksData
            }
        }
        setAdapter()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager = GridLayoutManager(context, GridLayoutConst.LANDSCAPE_SPAN_COUNT)
            manager.spanSizeLookup = GridLayoutLandscapeSpanSize
        } else {
            manager = GridLayoutManager(context, GridLayoutConst.PORTRAIT_SPAN_COUNT)
            manager.spanSizeLookup = GridLayoutPortraitSpanSize
        }
        binding.blocksContainer.run {
            layoutManager = manager
            adapter = adapterDelegate
        }
    }

    private fun onLinkBlockClicked(linkId: Int) {
        requireContext().openWebsite(getString(linkId))
    }

    private fun onAppLinkBlockClicked(linkId: Int) {
        startActivity<ArticleActivity> {
            setArticleType(Article.Type.PAGE)
            setUri(getString(linkId).toUri())
        }
    }

    private fun onPagerBlockClicked(number: Int) {
        (activity as MainActivity).setPagerItem(number)
    }

    private fun onTrainingBlockClicked() {
        startActivity<TrainingActivity>()
    }
}
