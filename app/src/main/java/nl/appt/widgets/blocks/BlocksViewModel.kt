package nl.appt.widgets.blocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result
import nl.appt.model.Block

open class BlocksViewModel(private val url: String) : ViewModel() {

    private val _blockResponse = MutableLiveData<Result<Block>>()

    val blockResponse: LiveData<Result<Block>> = _blockResponse

    fun getBlocksData() {
        _blockResponse.value = Result.loading()
        API.getBlocks(url) { response ->

            response.result?.let { block ->
                _blockResponse.value = Result.success(block)
            }

            response.error?.let { error ->
                _blockResponse.value = Result.error(error)
            }
        }
    }
}