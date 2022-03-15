package nl.appt.widgets.blocks

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.R
import nl.appt.adapters.descriptionAdapterDelegate
import nl.appt.adapters.listItemAdapterDelegate
import nl.appt.databinding.ViewCategoryBinding
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.openWebsite
import nl.appt.extensions.setBlock
import nl.appt.extensions.showError
import nl.appt.helpers.Result
import nl.appt.helpers.Status
import nl.appt.model.Block
import nl.appt.widgets.ToolbarFragment

abstract class BlocksFragment : ToolbarFragment() {

    override fun getLayoutId() = R.layout.view_category

    private var _binding: ViewCategoryBinding? = null

    private val binding get() = _binding!!

    abstract fun viewModel(): BlocksViewModel

    private val viewModel by lazy {
        viewModel()
    }

    private val adapterDelegate = ListDelegationAdapter(
        descriptionAdapterDelegate(),
        listItemAdapterDelegate { block ->
            onCategoryClicked(block)
        }
    )

    private var block: Block? = null

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

        setHasOptionsMenu(true)
        toolbar?.inflateMenu(R.menu.share)
        toolbar?.setOnMenuItemClickListener { item ->
            onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            startChooser(requireActivity())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startChooser(activity: FragmentActivity) {
        block?.let { block ->
            ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(R.string.action_share_article)
                .setType("text/plain")
                .setText(block.url)
                .startChooser()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setAdapter()
    }

    private fun setAdapter() {
        binding.itemsContainer.run {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDelegate
            addItemDecoration()
        }
    }

    private fun setAdapterData() {
        viewModel.blockResponse.observe(viewLifecycleOwner) { result ->
            onEvent(result)
        }
    }

    private fun onEvent(result: Result<Block>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let { block ->
                    this.block = block

                    val items = arrayListOf<Any>()
                    items.add(block.description)
                    items.addAll(block.children)

                    adapterDelegate.items = items
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

    override fun onResume() {
        super.onResume()

        if (viewModel.blockResponse.value?.status != Status.SUCCESS) {
            viewModel.getBlocksData()
        }
    }

    private fun onCategoryClicked(block: Block) {
        if (block.children.isNotEmpty()) {
            startActivity<BlockActivity> {
                setBlock(block)
            }
        } else if (block.url.isNotEmpty()) {
            requireContext().openWebsite(block.url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}