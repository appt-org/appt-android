package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    var blockResponse = MutableLiveData<Result<List<Any>>>()

    init {
        getBlocksData()
    }

    fun getBlocksData() {
        blockResponse.value = Result.loading()
        API.getBlocks(PATH_SERVICES_JSON) { response ->

            response.result?.let { block ->
                val list = arrayListOf<Any>()
                list.add(block.description)
                list.addAll(block.children)
                blockResponse.value = Result.success(list)
            }

            response.error?.let { error ->
                blockResponse.value = Result.error(error)
            }
        }
    }
}