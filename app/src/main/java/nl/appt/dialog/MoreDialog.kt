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
import nl.appt.helpers.headerAdapterDelegate
import nl.appt.helpers.textAdapterDelegate
import java.util.*
import kotlin.concurrent.timerTask

class MoreDialog(context: Context, layout: Int) : BottomSheetDialog(context) {

    private val adapterDelegate = ListDelegationAdapter(
        headerAdapterDelegate(),
        textAdapterDelegate {
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
            context.getString(R.string.bookmarks),
            context.getString(R.string.history),
            context.getString(R.string.settings),
            context.getString(R.string.cancel),
        )
        adapterDelegate.items = items

        view
    }

    init {
        setContentView(view)
    }
}