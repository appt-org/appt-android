package nl.appt.tabs.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.R
import nl.appt.adapters.blockAdapterDelegate
import nl.appt.adapters.descriptionAdapterDelegate
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setBlock
import nl.appt.extensions.showError
import nl.appt.helpers.GridLayoutConst
import nl.appt.helpers.GridLayoutSpanSize
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.model.Block
import nl.appt.widgets.BlockActivity
import nl.appt.widgets.ToolbarFragment

class ServicesFragment : ToolbarFragment() {

    override fun getTitle() = getString(R.string.diensten_toolbar_title)

    override fun getLayoutId() = R.layout.view_category

    private var _binding: ViewCategoryBinding? = null

    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(ServicesViewModel::class.java)
    }

    private val adapterDelegate = ListDelegationAdapter(
        descriptionAdapterDelegate(),
        blockAdapterDelegate { block ->
            onCategoryClicked(block)
        })

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
        val manager = GridLayoutManager(context, GridLayoutConst.SPAN_COUNT)
        manager.spanSizeLookup = GridLayoutSpanSize
        binding.itemsContainer.run {
            adapter = adapterDelegate
            layoutManager = manager
        }
    }

    private fun setAdapterData() {
        viewModel.blockResponse.observe(viewLifecycleOwner, { result ->
            onEvent(result)
        })
    }

    private fun onEvent(result: Result<List<Any>>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let { block ->
                    adapterDelegate.items = block
                    adapterDelegate.notifyDataSetChanged()
                }
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

    private fun onCategoryClicked(block: Block) {
        if (block.url.isNotEmpty()) {
            requireContext().openWebsite(block.url)
        } else {
            startActivity<BlockActivity> {
                setBlock(block)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}