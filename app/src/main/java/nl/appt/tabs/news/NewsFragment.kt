package nl.appt.tabs.news

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.*
import nl.appt.model.Article
import nl.appt.model.Filters
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class NewsFragment: ToolbarFragment() {

    private val REQUEST_CODE_FILTER = 1337

    private var page = 1
    private var pages: Int? = null
    private var filters: Filters? = null
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
        setHasOptionsMenu(true)

        toolbar?.inflateMenu(R.menu.filter)
        toolbar?.setOnMenuItemClickListener { item ->
            onOptionsItemSelected(item)
        }

        adapter.items = items

        view.recyclerView.adapter = adapter
        view.recyclerView.addItemDecoration()
        view.recyclerView.onInfiniteScroll {
            onInfiniteScroll()
        }
    }

    override fun onResume() {
        super.onResume()

        if (items.isEmpty()) {
//            getArticles()
        }
    }

    private fun onInfiniteScroll() {
        if (isLoading) {
            return
        }

        pages?.let { pages ->
            if (page <= pages) {
                getArticles()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            startActivity<FilterActivity>(REQUEST_CODE_FILTER) {
                setFilters(filters)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILTER) {
            data?.getFilters()?.let { filters ->
                this.filters = filters

                // RESULT_OK when filters should be applied.
                if (resultCode == RESULT_OK) {
                    reset()
                    getArticles()
                }
            }
        }
    }

    private fun reset() {
        page = 1
        items.clear()
        adapter.notifyDataSetChanged()
    }

    private fun getArticles() {
        isLoading = true

        API.getArticles(type = Article.Type.POST, filters = filters, page = page) { response ->
            this.pages = response.pages
            this.isLoading = false

            response.result?.let { articles ->
                page++
                items.addAll(articles)
                adapter.notifyDataSetChanged()
            }

            response.error?.let { error ->
                context?.showError(error)
            }
        }
    }
}
