package nl.appt.tabs.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    private val _blockResponse = MutableLiveData<Result<List<Any>>>()

    val blockResponse: LiveData<Result<List<Any>>> = _blockResponse

    fun getBlocksData() {
        _blockResponse.value = Result.loading()
        API.getBlocks(PATH_SERVICES_JSON) { response ->

            response.result?.let { block ->
                val list = arrayListOf<Any>()
                list.add(block.description)
                list.addAll(block.children)
                _blockResponse.value = Result.success(list)
            }

            response.error?.let { error ->
                _blockResponse.value = Result.error(error)
            }
        }
    }
}