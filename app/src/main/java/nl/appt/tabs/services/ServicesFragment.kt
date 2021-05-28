package nl.appt.tabs.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import nl.appt.R
import nl.appt.adapters.category.CategoryAdapter
import nl.appt.adapters.category.OnCategoryListener
import nl.appt.api.API
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.setBlock
import nl.appt.model.Block
import nl.appt.widgets.BlockActivity
import nl.appt.tabs.home.UserTypeFragment
import nl.appt.widgets.ToolbarFragment

class ServicesFragment : ToolbarFragment(), OnCategoryListener {

    override fun getTitle() = getString(R.string.diensten_toolbar_title)

    override fun getLayoutId() = R.layout.view_category

    private var _binding: ViewCategoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        isLoading = true
        API.getBlocks(BlockActivity.PATH_SERVICES_JSON) { response ->
            response.result?.let {
                isLoading = false
                val recyclerView = binding.blocksContainer
                recyclerView.layoutManager =
                    GridLayoutManager(requireContext(), UserTypeFragment.COLUMNS_NUMBER)
                val adapter = CategoryAdapter(it.children, this)
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onCategoryClicked(block: Block) {
        startActivity<BlockActivity> {
            setBlock(block)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}