package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.model.Block
import nl.appt.widgets.BlockActivity

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    var block = MutableLiveData<Block>()

    fun getBlocksData() {
        API.getBlocks(PATH_SERVICES_JSON) { response ->
            response.result?.let {
                block.value = it
            }
        }
    }
}