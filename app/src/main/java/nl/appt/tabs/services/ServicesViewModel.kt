package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    var blockResponse = MutableLiveData<Result<Any>>()

    init {
        getBlocksData()
    }

    private fun getBlocksData() {
        blockResponse.postValue(Result.loading(true))
        API.getBlocks(PATH_SERVICES_JSON) { response ->
            blockResponse.postValue(Result.loading(false))

            response.result?.let {
                blockResponse.value = Result.success(response.error, it)
            }

            response.error?.let { error ->
                blockResponse.value = Result.error(error)
            }
        }
    }
}