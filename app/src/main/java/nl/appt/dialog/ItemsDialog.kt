package nl.appt.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.*
import nl.appt.R
import nl.appt.extensions.addItemDecoration
import nl.appt.helpers.itemAdapterDelegate
import nl.appt.model.Item
import java.util.*
import kotlin.concurrent.timerTask

typealias ItemCallback = ((Item) -> Unit)

open class ItemsDialog(private val items: List<Item>) : BaseDialog() {

    var callback: ItemCallback? = null

    private val adapterDelegate = ListDelegationAdapter(
        itemAdapterDelegate { item ->
            callback?.let { callback ->
                callback(item)
            }

            Timer().schedule(timerTask {
                dismiss()
            }, 250)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_list, container, false)

        view.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDelegate
            addItemDecoration()
        }

        adapterDelegate.items = items

        return view
    }
}