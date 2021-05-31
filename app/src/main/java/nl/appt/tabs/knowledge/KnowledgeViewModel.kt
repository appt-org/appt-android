package nl.appt.tabs.knowledge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.appt.api.API
import nl.appt.helpers.Result
import nl.appt.model.Block

const val PATH_KNOWLEDGE_JSON = "wp-content/themes/appt/knowledgeBase.json"

class KnowledgeViewModel : ViewModel() {

    var blockResponse = MutableLiveData<Result<Block>>()

    init {
        getBlocksData()
    }

    private fun getBlocksData() {
        blockResponse.value = Result.loading()
        API.getBlocks(PATH_KNOWLEDGE_JSON) { response ->

            response.result?.let { block ->
                blockResponse.value = Result.success(block)
            }

            response.error?.let { error ->
                blockResponse.value = Result.error(error)
            }
        }
    }
}