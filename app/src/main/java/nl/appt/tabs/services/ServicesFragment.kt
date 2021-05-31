package nl.appt.tabs.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import nl.appt.R
import nl.appt.adapters.category.CategoryAdapter
import nl.appt.adapters.category.OnCategoryListener
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.setBlock
import nl.appt.extensions.showError
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.model.Block
import nl.appt.tabs.home.UserTypeFragment
import nl.appt.widgets.BlockActivity
import nl.appt.widgets.ToolbarFragment

class ServicesFragment : ToolbarFragment(), OnCategoryListener {

    override fun getTitle() = getString(R.string.diensten_toolbar_title)

    override fun getLayoutId() = R.layout.view_category

    private var _binding: ViewCategoryBinding? = null

    private val binding get() = _binding!!

    private val adapter by lazy {
        CategoryAdapter(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(ServicesViewModel::class.java)
    }

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
        setAdapterData()
    }

    private fun setAdapter() {
        binding.itemsContainer.layoutManager =
            GridLayoutManager(requireContext(), UserTypeFragment.COLUMNS_NUMBER)
        binding.itemsContainer.adapter = adapter
    }

    private fun setAdapterData() {
        viewModel.blockResponse.observe(viewLifecycleOwner, { result ->
            onEvent(result)
        })
    }

    private fun onEvent(result: Result<Block>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let { adapter.setData(it.children) }
                isLoading = false
            }
            Status.ERROR -> {
                context?.showError(result.fuelError)
                isLoading = false
            }
            Status.LOADING -> {
                isLoading = true
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