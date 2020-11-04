package nl.appt.tabs.knowledge

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.view.*
import nl.appt.R
import nl.appt.adapters.articleAdapterDelegate
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.showError
import nl.appt.model.Article
import nl.appt.model.Category
import nl.appt.model.Filters
import nl.appt.model.Tag
import nl.appt.tabs.training.TrainingActivity
import nl.appt.widgets.ToolbarFragment
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment: ToolbarFragment() {

    private val REQUEST_CODE_FILTER = 1337

    private val TAG = "KnowledgeFragment"
    private var items = arrayListOf<Any>()

    private var page = 1
    private var pages: Int? = null
    private var loaded = false

    private var filters: Filters? = null

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
        Log.d(TAG, "onActivityResult")

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTER) {
            (data?.getSerializableExtra("filters") as? Filters)?.let { filters ->
                this.filters = filters
                reset()
                getArticles()
            }
        }
    }

    override fun willShow() {
        if (loaded) {
            return
        }

        getArticles()
    }

    private fun reset() {
        page = 1
        items.clear()
        adapter.notifyDataSetChanged()
    }

    private fun getArticles() {
        setLoading(true)

        API.getArticles(filters = filters, page = page) { response ->
            Log.d(TAG, "Result: ${response.result}, error: ${response.error}")

            setLoading(false)

            response.pages?.let { pages ->
                this.page++
                this.pages = pages
            }
            response.result?.let { articles ->
                loaded = true
                items.addAll(articles)
                adapter.notifyDataSetChanged()
            }
            response.error?.let { error ->
                context?.showError(error)
            }
        }
    }
}
