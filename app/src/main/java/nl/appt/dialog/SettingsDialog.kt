package nl.appt.dialog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_list.view.*
import nl.appt.R
import nl.appt.extensions.addItemDecoration
import nl.appt.helpers.Preferences
import nl.appt.helpers.sliderAdapterDelegate
import nl.appt.model.Range

typealias ZoomScaleCallback = ((Float) -> Unit)

class SettingsDialog() : ListDialog(R.string.settings) {

    var zoomScaleCallback: ZoomScaleCallback? = null

    private val adapterDelegate = ListDelegationAdapter(
        sliderAdapterDelegate(100) { zoomScale ->
            zoomScaleCallback?.invoke(zoomScale)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDelegate
            addItemDecoration()
        }

        val zoomScale = Preferences.getZoomScale(view.context)
        val zoomScaleRange = Range(
            text = getString(R.string.settings_zoom),
            value = zoomScale,
            minimum = 0.5f,
            maximum = 2.0f,
            step = 0.25f
        )

        adapterDelegate.items = listOf(zoomScaleRange)
    }
}