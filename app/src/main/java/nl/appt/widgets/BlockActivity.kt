package nl.appt.widgets

import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.view.updatePadding
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

    private lateinit var binding: ViewCategoryBinding

    private val block by lazy {
        intent.getBlock()
    }

    private val adapterCategory by lazy {
        CategoryAdapter(this)
    }

    private val adapterSubCategory by lazy {
        SubCategoryAdapter(this)
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

    private fun setAdapter() {
        if (block.type == BLOCK_TYPE) {
            binding.itemsContainer.updatePadding(left = 32, right = 32)
            binding.itemsContainer.layoutManager = GridLayoutManager(this, UserTypeFragment.COLUMNS_NUMBER)
            adapterCategory.setData(block.children)
            binding.itemsContainer.adapter = adapterCategory
        }
        if (block.type == LIST_TYPE) {
            binding.itemsContainer.updatePadding(left = 0, right = 0)
            binding.itemsContainer.layoutManager = LinearLayoutManager(this)
            adapterSubCategory.setData(block.children)
            binding.itemsContainer.adapter = adapterSubCategory
            binding.itemsContainer.addItemDecoration()
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
                setTitle(block.title)
                setUri(block.url.toUri())
            }
        }
    }
}
