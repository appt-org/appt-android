package nl.appt.tabs.knowledge

import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.taxonomyAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.showError
import nl.appt.model.*
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
class FilterActivity: ToolbarActivity() {

    private var categories: List<Category>? = null
    private var tags: List<Tag>? = null

    private val adapter: ListDelegationAdapter<List<Any>> by lazy {
        ListDelegationAdapter(
            headerAdapterDelegate(),
            taxonomyAdapterDelegate { taxonomy ->
                // Ignored for now
            }
        )
    }

    override fun getLayoutId() = R.layout.view_list

    override fun getToolbarTitle(): String? {
        return getString(R.string.action_filter)
    }

    override fun onViewCreated() {
        super.onViewCreated()

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        getFilters()
    }

    private fun getFilters() {
        setLoading(true)

        API.getFilters { response ->
            setLoading(false)

            response.result?.let { filters ->
                this.categories = filters.categories
                this.tags = filters.tags

                val items = mutableListOf<Any>()
                items.add("Categorieën")
                items.addAll(filters.categories)
                items.add("Trefwoorden")
                items.addAll(filters.tags)
                this.adapter.items = items
                this.adapter.notifyDataSetChanged()
            }
            response.error?.let { error ->
                showError(error)
            }
        }
    }
}