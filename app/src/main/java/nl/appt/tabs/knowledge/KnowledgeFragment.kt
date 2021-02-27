package nl.appt.tabs.knowledge

import android.os.Bundle
import android.view.*
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.*
import nl.appt.model.Article
import nl.appt.tabs.news.ArticleActivity
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 24/02/2021
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment: ToolbarFragment() {

    private val KENNISBANK_ID = 676
    private var items = arrayListOf<Any>()

    private val adapter: ListDelegationAdapter<List<Any>> by lazy {
        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            itemAdapterDelegate<Article> { article ->
                startActivity<ArticleActivity> {
                    setArticleType(article.type)
                    setId(article.id)
                }
            }
        )
        adapter.items = items
        adapter
    }

    override fun getLayoutId() = R.layout.view_list

    override fun getTitle() = getString(R.string.tab_knowledge)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.items = items

        view.recyclerView.adapter = adapter
        view.recyclerView.addItemDecoration()
    }

    override fun onResume() {
        super.onResume()

        if (items.isEmpty()) {
            getArticles()
        }
    }

    private fun getArticles() {
        isLoading = true

        API.getArticles(type = Article.Type.PAGE, parentId = KENNISBANK_ID) { response ->
            this.isLoading = false

            response.result?.let { articles ->
                items.addAll(articles)
                adapter.notifyDataSetChanged()
            }

            response.error?.let { error ->
                context?.showError(error)
            }
        }
    }
}
