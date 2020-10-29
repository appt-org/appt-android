package nl.appt.tabs.knowledge

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.view.*
import nl.appt.R
import nl.appt.adapters.ItemRecyclerViewAdapter
import nl.appt.api.API
import nl.appt.model.Article
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment: ToolbarFragment(), ItemRecyclerViewAdapter.Callback<Article> {

    private val TAG = "TrainingFragment"
    private var adapter = ItemRecyclerViewAdapter(listener = this)
    private var loaded = false

    override fun getViewId(): Int {
        return R.layout.fragment_list
    }

    override fun getTitle(): String? {
        return getString(R.string.title_knowledge)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@KnowledgeFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
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
        }
    }

    private fun onArticles(articles: List<Article>) {
        loaded = true

        Log.d(TAG, "onArticles: $articles")
        adapter.add(articles)
    }

    override fun onItemClicked(item: Article) {
        startActivity<ArticleActivity> {
            putExtra("id", item.id)
        }
    }
}
