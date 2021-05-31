package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    init {
        getBlocksData()
    }

    var blockResponse = MutableLiveData<Result<Int>>()

    private fun getBlocksData() {
        API.getBlocks(PATH_SERVICES_JSON) { response ->
            response.result?.let { block ->
                blockResponse.value = Result.success(block)
            }

            response.error?.let { error ->
                blockResponse.value = Result.error(error)
            }
        }
    }
}