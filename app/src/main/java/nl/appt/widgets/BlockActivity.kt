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
import nl.appt.extensions.getBlock
import nl.appt.extensions.setArticleType
import nl.appt.extensions.setBlock
import nl.appt.extensions.setUri
import nl.appt.extensions.addItemDecoration
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
        when (block.type) {
            BLOCK_TYPE -> {
                binding.itemsContainer.run {
                    updatePadding(
                        left = resources.getDimensionPixelSize(R.dimen.padding_recycler_view),
                        right = resources.getDimensionPixelSize(R.dimen.padding_recycler_view)
                    )
                    layoutManager =
                        GridLayoutManager(context, UserTypeFragment.COLUMNS_NUMBER)
                    adapter = adapterCategory
                }
                adapterCategory.setData(block.children)
            }
            LIST_TYPE -> {
                binding.itemsContainer.run {
                    updatePadding(left = 0, right = 0)
                    layoutManager = LinearLayoutManager(context)
                    adapter = adapterSubCategory
                    addItemDecoration()
                }
                adapterSubCategory.setData(block.children)
            }
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
                title = block.title
                setUri(block.url.toUri())
            }
        }
    }
}
