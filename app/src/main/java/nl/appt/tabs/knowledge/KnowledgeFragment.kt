package nl.appt.tabs.knowledge

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
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.model.Block
import nl.appt.widgets.BlockActivity
import nl.appt.widgets.ToolbarFragment

/**
 * Created by Jan Jaap de Groot on 24/02/2021
 * Copyright 2020 Stichting Appt
 */
class KnowledgeFragment : ToolbarFragment() {

    override fun getTitle() = getString(R.string.tab_knowledge)

    override fun getLayoutId() = R.layout.view_category

    private var _binding: ViewCategoryBinding? = null

    private val binding get() = _binding!!

    private val adapterDelegate = ListDelegationAdapter(
        descriptionAdapterDelegate(),
        blockAdapterDelegate { block ->
            onCategoryClicked(block)
        })

    private val viewModel by lazy {
        ViewModelProvider(this).get(KnowledgeViewModel::class.java)
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
        val manager = GridLayoutManager(context, GridLayoutConst.SPAN_COUNT)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == GridLayoutConst.HEADER_POSITION) {
                    GridLayoutConst.SPAN_COUNT
                } else GridLayoutConst.DEFAULT_SPAN_SIZE
            }
        }
        binding.itemsContainer.run {
            layoutManager = manager
            adapter = adapterDelegate
        }
    }

    private fun setAdapterData() {
        viewModel.blockResponse.observe(viewLifecycleOwner, { result ->
            onEvent(result)
        })
    }

    private fun onEvent(result: Result<Block>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let { block ->
                    val list = arrayListOf<Any>()
                    list.add(block.description)
                    list.addAll(block.children)
                    adapterDelegate.items = list
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
