package nl.appt.tabs.knowledge

import androidx.lifecycle.ViewModelProvider
import nl.appt.R
import nl.appt.widgets.blocks.BlocksFragment
import nl.appt.widgets.blocks.BlocksViewModel

class KnowledgeViewModel : BlocksViewModel("wp-content/themes/appt/knowledgeBase.json")

class KnowledgeFragment : BlocksFragment() {

    override fun getTitle(): String {
        return getString(R.string.knowledge_toolbar_title)
    }

    override fun viewModel(): BlocksViewModel {
        return ViewModelProvider(this).get(KnowledgeViewModel::class.java)
    }
}
