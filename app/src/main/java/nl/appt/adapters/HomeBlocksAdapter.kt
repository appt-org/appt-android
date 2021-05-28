package nl.appt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.appt.R
import nl.appt.databinding.ViewBlockBinding
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel

private const val TYPE_LINK = 0
private const val TYPE_TRAINING = 1
private const val TYPE_PAGER = 2

private const val ERROR_INVALID_DATA_TYPE = "Invalid type of data "
private const val ERROR_INVALID_VIEW_TYPE = "Invalid view type"

class HomeBlocksAdapter(private val userBlocksData: ArrayList<Any>, private val onBlockListener: OnBlockListener) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    inner class LinkViewHolder(itemView: View, private val onBlockListener: OnBlockListener) :
        BaseViewHolder<HomeLinkModel>(itemView) {

        val binding = ViewBlockBinding.bind(itemView)

        override fun bind(item: HomeLinkModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onLinkBlockClicked(item.link)
            }
        }
    }

    inner class TrainingViewHolder(itemView: View, private val onBlockListener: OnBlockListener) :
        BaseViewHolder<HomeTrainingModel>(itemView) {

        val binding = ViewBlockBinding.bind(itemView)

        override fun bind(item: HomeTrainingModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onTrainingBlockClicked()
            }
        }
    }

    inner class PagerViewHolder(itemView: View, private val onBlockListener: OnBlockListener) :
        BaseViewHolder<HomePagerModel>(itemView) {

        val binding = ViewBlockBinding.bind(itemView)

        override fun bind(item: HomePagerModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onPagerBlockClicked(item.pagerPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_LINK -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_block, parent, false)
                LinkViewHolder(view, onBlockListener)
            }
            TYPE_TRAINING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_block, parent, false)
                TrainingViewHolder(view, onBlockListener)
            }
            TYPE_PAGER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_block, parent, false)
                PagerViewHolder(view, onBlockListener)
            }
            else -> throw IllegalArgumentException(ERROR_INVALID_VIEW_TYPE)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = userBlocksData[position]
        when (holder) {
            is LinkViewHolder -> holder.bind(element as HomeLinkModel)
            is TrainingViewHolder -> holder.bind(element as HomeTrainingModel)
            is PagerViewHolder -> holder.bind(element as HomePagerModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (userBlocksData[position]) {
            is HomeLinkModel -> TYPE_LINK
            is HomeTrainingModel -> TYPE_TRAINING
            is HomePagerModel -> TYPE_PAGER
            else -> throw IllegalArgumentException(ERROR_INVALID_DATA_TYPE + position)
        }
    }

    override fun getItemCount(): Int {
        return userBlocksData.size
    }
}

interface OnBlockListener {
    fun onLinkBlockClicked(link: String)
    fun onPagerBlockClicked(number: Int)
    fun onTrainingBlockClicked()
}

