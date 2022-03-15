package nl.appt.widgets.blocks

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.R
import nl.appt.adapters.blockAdapterDelegate
import nl.appt.adapters.descriptionAdapterDelegate
import nl.appt.adapters.listItemAdapterDelegate
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.getBlock
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setBlock
import nl.appt.helpers.GridLayoutConst
import nl.appt.helpers.GridLayoutLandscapeSpanSize
import nl.appt.helpers.GridLayoutPortraitSpanSize
import nl.appt.model.Block
import nl.appt.widgets.ToolbarActivity

private const val BLOCK_TYPE = "blocks"
private const val LIST_TYPE = "list"

class BlockActivity : ToolbarActivity() {

    private lateinit var binding: ViewCategoryBinding

    private lateinit var manager: GridLayoutManager

    private val block by lazy {
        intent.getBlock()
    }

    private val blockAdapterDelegate = ListDelegationAdapter(
        descriptionAdapterDelegate(),
        blockAdapterDelegate { block ->
            onBlockClicked(block)
        })

    private val listAdapterDelegate = ListDelegationAdapter(
        descriptionAdapterDelegate(),
        listItemAdapterDelegate { block ->
            onBlockClicked(block)
        })

    private val list = arrayListOf<Any>()

    override fun getToolbarTitle() = block.title

    override fun getLayoutId() = R.layout.view_category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewCategoryBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        onViewCreated()
        setAdapterData()
        setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                onShare()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onShare() {
        val block = this.block

        ShareCompat.IntentBuilder.from(this)
            .setChooserTitle(R.string.action_share_article)
            .setType("text/plain")
            .setSubject(block.title)
            .setText(block.url)
            .startChooser()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setAdapter()
    }

    private fun setAdapter() {
        when (block.type) {
            BLOCK_TYPE -> {
                binding.itemsContainer.run {
                    setBlockAdapter()
                }
            }
            LIST_TYPE -> {
                binding.itemsContainer.run {
                    listAdapterDelegate.items = list
                    adapter = listAdapterDelegate
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration()
                }
            }
        }
    }

    private fun setBlockAdapter() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager = GridLayoutManager(this, GridLayoutConst.LANDSCAPE_SPAN_COUNT)
            manager.spanSizeLookup = GridLayoutLandscapeSpanSize
        } else {
            manager = GridLayoutManager(this, GridLayoutConst.PORTRAIT_SPAN_COUNT)
            manager.spanSizeLookup = GridLayoutPortraitSpanSize
        }
        binding.itemsContainer.run {
            layoutManager = manager
            adapter = blockAdapterDelegate
        }
    }

    private fun setAdapterData() {
        list.add(block.description)
        list.addAll(block.children)
    }

    private fun onBlockClicked(block: Block) {
        if (block.children.isNotEmpty()) {
            startActivity<BlockActivity> {
                setBlock(block)
            }
        } else {
            openWebsite(block.url)
        }
    }
}
