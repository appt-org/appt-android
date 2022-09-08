package nl.appt.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.*
import nl.appt.R
import nl.appt.extensions.addItemDecoration
import nl.appt.helpers.itemAdapterDelegate
import nl.appt.model.Item
import java.util.*
import kotlin.concurrent.timerTask

typealias ItemCallback = ((Item) -> Unit)

open class ItemsDialog(context: Context, private val items: List<Item>) : BottomSheetDialog(context) {

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

    private val view: View by lazy {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_list, null)

        view.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDelegate
            addItemDecoration()
        }

        adapterDelegate.items = items

        view
    }

    init {
        setContentView(view)
        behavior.peekHeight = 1000
    }
}