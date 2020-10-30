package nl.appt.tabs.knowledge

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import nl.appt.R
import nl.appt.adapters.ItemRecyclerViewAdapter
import nl.appt.api.API
import nl.appt.extensions.showError
import nl.appt.model.*
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
class FilterActivity: ToolbarActivity(), ItemRecyclerViewAdapter.Callback<Item> {

    private var categories: List<Category>? = null
    private var tags: List<Tag>? = null

    private var adapter = ItemRecyclerViewAdapter(listener = this)

    override fun getLayoutId() = R.layout.fragment_list

    override fun getToolbarTitle(): String? {
        return getString(R.string.action_filter)
    }

    override fun onViewCreated() {
        super.onViewCreated()

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FilterActivity.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        getFilters()
    }

    private fun getFilters() {
        setLoading(true)

        API.getFilters { response ->
            setLoading(false)

            response.result?.let { filters ->
                adapter.add(Header("Categorieën"))
                adapter.add(filters.categories)
                adapter.add(Header("Trefwoorden"))
                adapter.add(filters.tags)

                this.categories = filters.categories
                this.tags = filters.tags
            }
            response.error?.let { error ->
                showError(error)
            }
        }
    }

    override fun onItemClicked(item: Item) {
        if (item is Category) {

        } else if (item is Tag) {

        }
    }
}