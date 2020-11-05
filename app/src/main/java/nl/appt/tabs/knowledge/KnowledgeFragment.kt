package nl.appt.tabs.knowledge

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.setTraversalOrder
import nl.appt.adapters.articleAdapterDelegate
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.onInfiniteScroll
import nl.appt.extensions.showError
import nl.appt.model.Filters
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment: ToolbarFragment() {

    private val REQUEST_CODE_FILTER = 1337

    private var page = 1
    private var pages: Int? = null
    private var filters: Filters? = null
    private var items = arrayListOf<Any>()

    private val adapter: ListDelegationAdapter<List<Any>> by lazy {
        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            articleAdapterDelegate { article ->
                startActivity<ArticleActivity> {
                    putExtra("id", article.id)
                }
            }
        )
        adapter.items = items
        adapter
    }

    override fun getLayoutId() = R.layout.view_list

    override fun getTitle(): String? {
        return getString(R.string.title_knowledge)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.inflateMenu(R.menu.filter)
        toolbar?.setOnMenuItemClickListener { item ->
            onOptionsItemSelected(item)
        }

        adapter.items = items

        view.recyclerView.adapter = adapter
        view.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.recyclerView.onInfiniteScroll {
            onInfiniteScroll()
        }

        if (savedInstanceState != null) {
            getArticles()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            startActivity<FilterActivity>(REQUEST_CODE_FILTER) {
                putExtra("filters", filters)
            }
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILTER) {
            (data?.getSerializableExtra("filters") as? Filters)?.let { filters ->
                this.filters = filters

                // RESULT_OK when filters should be applied.
                if (resultCode == RESULT_OK) {
                    reset()
                    getArticles()
                }
            }
        }
    }

    override fun willShow() {
        if (items.isEmpty()) {
            getArticles()
        }
    }

    private fun reset() {
        page = 1
        items.clear()
        adapter.notifyDataSetChanged()
    }

    private fun getArticles() {
        isLoading = true

        API.getArticles(filters = filters, page = page) { response ->
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
