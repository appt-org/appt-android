package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result
import nl.appt.model.Block

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    var blockResponse = MutableLiveData<Result<Block>>()

    init {
        getBlocksData()
    }

    private fun getBlocksData() {
        blockResponse.value = Result.loading()
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