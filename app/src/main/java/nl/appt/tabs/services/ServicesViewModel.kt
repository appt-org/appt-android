package nl.appt.tabs.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.core.FuelError
import nl.appt.api.API
import nl.appt.helpers.Errors
import nl.appt.model.Block

private const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"

class ServicesViewModel : ViewModel() {

    init {
        getBlocksData()
    }

    var blockResponse = MutableLiveData<Block>()

    val errorEvent = MutableLiveData<Errors<FuelError>>()

    private fun getBlocksData() {
        API.getBlocks(PATH_SERVICES_JSON) { response ->
            response.result?.let { block ->
                blockResponse.value = block
            }

            response.error?.let { error ->
                errorEvent.value = Errors.error(error)
            }
        }
    }
}