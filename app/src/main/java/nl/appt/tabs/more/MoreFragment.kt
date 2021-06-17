package nl.appt.tabs.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.FragmentActivity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.moreItemAdapterDelegate
import nl.appt.databinding.ViewMeerListBinding
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setArticleType
import nl.appt.extensions.setSlug
import nl.appt.extensions.setTitle
import nl.appt.extensions.setUri
import nl.appt.model.Article
import nl.appt.model.Topic
import nl.appt.tabs.news.ArticleActivity
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */

private const val TEXT_FOR_CHOOSER = "https://appt.nl/app"
private const val TYPE_FOR_CHOOSER = "text/plain"

class MoreFragment : ToolbarFragment() {

    override fun getLayoutId() = R.layout.view_meer_list

    override fun getTitle() = getString(R.string.tab_more)

    private var _binding: ViewMeerListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewMeerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        toolbar?.inflateMenu(R.menu.share)
        toolbar?.setOnMenuItemClickListener { item ->
            onOptionsItemSelected(item)
        }

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            moreItemAdapterDelegate { topic ->
                topic.url?.let { url ->
                    requireContext().openWebsite(url)
                }
                topic.slug?.let { slug ->
                    startActivity<ArticleActivity> {
                        setArticleType(Article.Type.PAGE)
                        setSlug(slug)
                    }
                }
                topic.appLink?.let { appLink ->
                    startActivity<ArticleActivity> {
                        setArticleType(Article.Type.PAGE)
                        setTitle(Topic.CONTACT.title(requireContext()))
                        setUri(appLink)
                    }
                }

                if (topic.userProfile) {
                   startActivity<ProfileActivity>()
                }
            }
        )
        adapter.items = items

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            startChooser(requireActivity())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startChooser(activity: FragmentActivity) {
        ShareCompat.IntentBuilder.from(activity)
            .setType(TYPE_FOR_CHOOSER)
            .setText(TEXT_FOR_CHOOSER)
            .startChooser()
    }

    companion object {
        private val items = listOf(
        //  Topic.PROFILE,
            "Over deze app",
            Topic.SOURCE,
            Topic.SPONSOR,
            Topic.CONTACT,
            "Juridisch",
            Topic.TERMS,
            Topic.PRIVACY,
            Topic.ACCESSIBILITY
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
