package nl.appt.tabs.information

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.topicAdapterDelegate
import nl.appt.extensions.openWebsite
import nl.appt.model.Article
import nl.appt.model.Topic
import nl.appt.tabs.knowledge.ArticleActivity
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class InformationFragment : ToolbarFragment() {

    override fun getLayoutId() = R.layout.view_list

    override fun getTitle(): String? {
        return getString(R.string.title_information)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            topicAdapterDelegate { topic ->
                topic.url?.let { url ->
                    context?.openWebsite(url)
                }
                topic.slug?.let { slug ->
                    startActivity<ArticleActivity> {
                        putExtra("type", Article.Type.PAGE)
                        putExtra("slug", slug)
                    }
                }
            }
        )
        adapter.items = items

        view.recyclerView.adapter  = adapter
        view.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    companion object {
        private val items = listOf(
            "Juridisch",
            Topic.TERMS,
            Topic.PRIVACY,
            Topic.ACCESSIBILITY,
            "Overig",
            Topic.SOURCE,
            Topic.SPONSOR,
        )
    }
}
