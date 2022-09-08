package nl.appt.dialog

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import nl.appt.database.History
import nl.appt.model.Item
import nl.appt.model.WebViewModel

class HistoryDialog : PageDialog<History>(Item.HISTORY.title) {

    private val model: WebViewModel by activityViewModels()

    override fun getData(): LiveData<List<History>> {
        return model.allHistory()
    }
}