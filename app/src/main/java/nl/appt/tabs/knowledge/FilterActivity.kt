package nl.appt.tabs.knowledge

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    override fun getToolbarTitle(): String? {
        return getString(R.string.action_filter)
    }

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
            val intent = Intent()
            intent.putExtra("filters", filters)
            setResult(RESULT_OK, intent)
            finish()
            return true
        }
        return false
    }

    private fun getFilters() {
        (intent.getSerializableExtra("filters") as? Filters)?.let { filters ->
            onFilters(filters)
        } ?: run {
            setLoading(true)

            API.getFilters { response ->
                setLoading(false)

                response.result?.let { filters ->
                    onFilters(filters)
                }
                response.error?.let { error ->
                    showError(error)
                }
            }
        }
    }

    private fun onFilters(filters: Filters) {
        this.filters = filters

        val items = arrayListOf<Any>()
        items.add("Categorieën")
        items.addAll(filters.categories)
        items.add("Trefwoorden")
        items.addAll(filters.tags)

        this.adapter.items = items
        this.adapter.notifyDataSetChanged()
    }
}