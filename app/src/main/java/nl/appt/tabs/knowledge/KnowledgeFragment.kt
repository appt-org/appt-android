package nl.appt.tabs.knowledge

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
import nl.appt.model.Tag
import nl.appt.tabs.training.TrainingActivity
import nl.appt.widgets.ToolbarFragment
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment: ToolbarFragment() {

    private val TAG = "TrainingFragment"
    private var items = mutableListOf<Any>()
    private var loaded = false

    private var categories: List<Category>? = null
    private var tags: List<Tag>? = null

    private val adapter: ListDelegationAdapter<List<Any>> by lazy {
        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            articleAdapterDelegate { article ->
                startActivity<TrainingActivity> {
                    startActivity<ArticleActivity> {
                        putExtra("id", article.id)
                    }
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
        when (item.itemId) {
            R.id.action_filter -> {
                startActivity<FilterActivity> {
                    putExtra("categories", arrayOf<Any>())
                    putExtra("tags", arrayOf<Serializable>())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun willShow() {
        if (loaded) {
            return
        }

        getArticles()
    }

    private fun getArticles() {
        setLoading(true)

        API.getArticles(page = 1) { response ->
            Log.d(TAG, "Result: ${response.result}, error: ${response.error}")

            setLoading(false)

            response.result?.let { articles ->
                onArticles(articles)
            }
            response.error?.let { error ->
                context?.showError(error)
            }
        }
    }

    private fun onArticles(articles: List<Article>) {
        loaded = true

        items.addAll(articles)
        adapter.notifyDataSetChanged()
    }
}
