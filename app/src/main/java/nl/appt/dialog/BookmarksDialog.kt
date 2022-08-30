package nl.appt.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.*
import nl.appt.R
import nl.appt.database.Bookmark
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.isLoading
import nl.appt.helpers.bookmarkAdapterDelegate
import nl.appt.model.WebViewModel
import java.util.*
import kotlin.concurrent.timerTask

class BookmarksDialog : BottomSheetDialogFragment() {

    private val model: WebViewModel by activityViewModels()

    var callback: ((Bookmark) -> Unit)? = null

    private val adapterDelegate = ListDelegationAdapter(
        bookmarkAdapterDelegate { bookmark ->
            callback?.let { callback ->
                callback(bookmark)
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
        return inflater.inflate(R.layout.layout_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading = true

        model.all().observe(this) { bookmarks ->
            isLoading = false

            adapterDelegate.items = bookmarks
            view.recyclerView.run {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterDelegate
                addItemDecoration()
            }
        }
    }
}