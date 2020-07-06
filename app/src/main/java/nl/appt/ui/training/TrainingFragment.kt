package nl.appt.ui.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_training.view.*
import nl.appt.R
import nl.appt.accessibility.accessibility

class TrainingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training, container, false)

        view.text.accessibility.label = "Training screen"
        view.text.accessibility.action = "Nothing"

        return view
    }
}