package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.api.Response
import nl.appt.model.Block

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    var blockResponse = MutableLiveData<Response<Block>>()

    fun getBlocksData() {
        API.getBlocks(PATH_SERVICES_JSON) { response ->
            blockResponse.value = response
        }
    }
}