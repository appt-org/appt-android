package nl.appt.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.*
import nl.appt.extensions.addItemDecoration
import nl.appt.helpers.actionAdapterDelegate
import nl.appt.model.Action
import java.util.*
import kotlin.concurrent.timerTask

class MoreDialog(context: Context, layout: Int) : BottomSheetDialog(context) {

    var callback: ((Action) -> Unit)? = null

    private val adapterDelegate = ListDelegationAdapter(
        actionAdapterDelegate { action ->
            callback?.let { callback ->
                callback(action)
            }

            Timer().schedule(timerTask {
                dismiss()
            }, 250)
        }
    )

    private val view: View by lazy {
        val view = LayoutInflater.from(context).inflate(layout, null)

        view.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDelegate
            addItemDecoration()
        }

        val items = listOf(
            Action.HOME,
            Action.BOOKMARKS,
            Action.HISTORY,
            Action.SETTINGS,
            Action.RELOAD,
            Action.CANCEL
        )
        adapterDelegate.items = items

        view
    }

    init {
        setContentView(view)
        behavior.peekHeight = 1000
    }
}