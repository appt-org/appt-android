package nl.appt.widgets

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import nl.appt.R
import nl.appt.adapters.category.CategoryAdapter
import nl.appt.adapters.category.OnCategoryListener
import nl.appt.adapters.category.SubCategoryAdapter
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.*
import nl.appt.model.Article
import nl.appt.model.Block
import nl.appt.tabs.home.UserTypeFragment
import nl.appt.tabs.news.ArticleActivity

private const val BLOCK_TYPE = "blocks"
private const val LIST_TYPE = "list"

class BlockActivity : ToolbarActivity(), OnCategoryListener {

    companion object{
        const val PATH_KNOWLEDGE_JSON = "wp-content/themes/appt/knowledgeBase.json"
        const val PATH_SERVICES_JSON = "wp-content/themes/appt/services.json"
    }

    private lateinit var binding: ViewCategoryBinding

    private val block by lazy {
        intent.getBlock()
    }

    override fun getToolbarTitle() = block.title

    override fun getLayoutId() = R.layout.view_category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.title.text = block.description
        onViewCreated()
        setAdapter()
    }

    private fun setAdapter(){
        if(block.type == BLOCK_TYPE){
            binding.blocksContainerSubtitle.visibility = View.GONE
            binding.blocksContainer.visibility = View.VISIBLE

            val recyclerView = binding.blocksContainer
            recyclerView.layoutManager = GridLayoutManager(this, UserTypeFragment.COLUMNS_NUMBER)
            val adapter = CategoryAdapter(block.children, this)
            recyclerView.adapter = adapter
        }
        if (block.type == LIST_TYPE){
            binding.blocksContainer.visibility = View.GONE
            binding.blocksContainerSubtitle.visibility = View.VISIBLE

            val recyclerView = binding.blocksContainerSubtitle
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = SubCategoryAdapter(block.children, this)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration()
        }
    }

    override fun onCategoryClicked(block: Block) {
        if (block.children.isNotEmpty()) {
            startActivity<BlockActivity> {
                setBlock(block)
            }
        } else {
            startActivity<ArticleActivity> {
                setArticleType(Article.Type.PAGE)
                setUri(block.url.toUri())
            }
        }
    }
}