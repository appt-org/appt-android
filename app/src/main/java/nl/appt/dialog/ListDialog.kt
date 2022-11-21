package nl.appt.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_list_dialog.view.*
import nl.appt.R

abstract class ListDialog(val title: Int) : BaseDialog() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_list_dialog, container, false)

        view.titleView.setText(title)

        view.closeButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}