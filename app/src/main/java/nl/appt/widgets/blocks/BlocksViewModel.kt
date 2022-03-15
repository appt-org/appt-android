package nl.appt.widgets.blocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result

open class BlocksViewModel(private val url: String) : ViewModel() {

    private val _blockResponse = MutableLiveData<Result<List<Any>>>()

    val blockResponse: LiveData<Result<List<Any>>> = _blockResponse

    fun getBlocksData() {
        _blockResponse.value = Result.loading()
        API.getBlocks(url) { response ->

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