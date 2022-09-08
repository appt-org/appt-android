package nl.appt.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.appt.model.WebPage

class WebPageDialog(val pages: List<WebPage>) : PageDialog<WebPage>() {

    override fun getData(): LiveData<List<WebPage>> {
        return MutableLiveData(pages)
    }
}