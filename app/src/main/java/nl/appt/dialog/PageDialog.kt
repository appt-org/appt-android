package nl.appt.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.recyclerView
import nl.appt.database.Page
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.isLoading
import nl.appt.helpers.pageAdapterDelegate

abstract class PageDialog<T: Page>(title: Int) : ListDialog(title) {

    var onClick: ((Page) -> Unit)? = null
    var onLongClick: ((Page) -> Unit)? = null

    private val adapterDelegate = ListDelegationAdapter(
        pageAdapterDelegate(onClick = { page: Page ->
            onClick?.let { it(page)}
        }, onLongClick = { page: Page ->
            onLongClick?.let { it(page) }
        })
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isLoading = true

        getData().observe(this) { items ->
            isLoading = false

            // TODO: Add empty state

            adapterDelegate.items = items
            view.recyclerView?.run {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterDelegate
                addItemDecoration()
            }
        }
    }

    abstract fun getData(): LiveData<List<T>>
}