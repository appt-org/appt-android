package nl.appt.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.appt.model.Item
import nl.appt.model.WebPage

class WebPageDialog(val item: Item, val pages: List<WebPage>) : PageDialog<WebPage>(item.title) {

    override fun getData(): LiveData<List<WebPage>> {
        return MutableLiveData(pages)
    }
}