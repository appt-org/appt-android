package nl.appt.tabs.news

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_list.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.announce
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.taxonomyAdapterDelegate
import nl.appt.api.API
import nl.appt.extensions.getFilters
import nl.appt.extensions.setFilters
import nl.appt.extensions.showError
import nl.appt.helpers.Events
import nl.appt.model.Filters
import nl.appt.model.names
import nl.appt.model.selected
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
class FilterActivity: ToolbarActivity() {

    private var filters: Filters? = null

    private val adapter: ListDelegationAdapter<List<Any>> by lazy {
        ListDelegationAdapter(
            headerAdapterDelegate(),
            taxonomyAdapterDelegate { taxonomy ->
                // Ignored for now
                Log.d("Filters", "$filters")
            }
        )
    }

    override fun getLayoutId() = R.layout.view_list

    override fun getToolbarTitle() = getString(R.string.action_filter)

    override fun onViewCreated() {
        super.onViewCreated()

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        getFilters()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.apply, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_apply) {
            applyFilters()
            return true
        }
        return false
    }

    private fun applyFilters() {
        events.log(Events.Category.filters, mapOf(
            "categories" to filters?.categories?.selected?.names,
            "tags" to filters?.tags?.selected?.names
        ))

        val intent = Intent()
        intent.setFilters(filters)
        setResult(RESULT_OK, intent)

        finish()
    }

    private fun getFilters() {
        intent.getFilters()?.let { filters ->
            onFilters(filters)
        } ?: run {
            isLoading = true

            API.getFilters { response ->
                isLoading = false

                response.result?.let { filters ->
                    onFilters(filters)
                }
                response.error?.let { error ->
                    showError(error) {
                        finish()
                    }
                }
            }
        }
    }

    private fun onFilters(filters: Filters) {
        this.filters = filters

        Accessibility.announce(this, "Er zijn ${filters.categories.size + filters.tags.size} filteropties geladen.")

        val items = arrayListOf<Any>()
        items.add("Categorieën")
        items.addAll(filters.categories)
        items.add("Trefwoorden")
        items.addAll(filters.tags)

        this.adapter.items = items
        this.adapter.notifyDataSetChanged()
    }
}