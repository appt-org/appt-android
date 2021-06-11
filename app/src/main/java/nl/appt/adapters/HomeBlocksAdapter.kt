package nl.appt.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.appt.databinding.ViewBlockBinding
import nl.appt.model.HomeAppLinkModel
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel

private const val TYPE_LINK = 0
private const val TYPE_TRAINING = 1
private const val TYPE_PAGER = 2
private const val TYPE_APP_LINK = 3

class HomeBlocksAdapter(
    private val userBlocksData: ArrayList<Any>,
    private val onBlockListener: OnBlockListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = ViewBlockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return when (viewType) {
            TYPE_LINK -> {
                LinkViewHolder(binding)
            }
            TYPE_TRAINING -> {
                TrainingViewHolder(binding)
            }
            TYPE_PAGER -> {
                PagerViewHolder(binding)
            }
            TYPE_APP_LINK -> {
                AppLinkViewHolder(binding)
            }
            else -> throw IllegalArgumentException(BaseViewHolder.ERROR_INVALID_VIEW_TYPE + viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = userBlocksData[position]
        when (holder) {
            is LinkViewHolder -> holder.bind(element as HomeLinkModel)
            is TrainingViewHolder -> holder.bind(element as HomeTrainingModel)
            is PagerViewHolder -> holder.bind(element as HomePagerModel)
            is AppLinkViewHolder -> holder.bind(element as HomeAppLinkModel)
            else -> throw IllegalArgumentException(BaseViewHolder.ERROR_INVALID_HOLDER + holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (userBlocksData[position]) {
            is HomeLinkModel -> TYPE_LINK
            is HomeTrainingModel -> TYPE_TRAINING
            is HomePagerModel -> TYPE_PAGER
            is HomeAppLinkModel -> TYPE_APP_LINK
            else -> throw IllegalArgumentException(BaseViewHolder.ERROR_INVALID_DATA_TYPE + position)
        }
    }

    override fun getItemCount(): Int {
        return userBlocksData.size
    }

    inner class LinkViewHolder(private val binding: ViewBlockBinding) :
        BaseViewHolder<HomeLinkModel>(binding.root) {

        override fun bind(item: HomeLinkModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.contentDescription = item.title + IMAGE
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onLinkBlockClicked(item.link)
            }
        }
    }

    inner class AppLinkViewHolder(private val binding: ViewBlockBinding) :
        BaseViewHolder<HomeAppLinkModel>(binding.root) {

        override fun bind(item: HomeAppLinkModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.contentDescription = item.title + IMAGE
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onAppLinkBlockClicked(item.title, item.link)
            }
        }
    }

    inner class TrainingViewHolder(private val binding: ViewBlockBinding) :
        BaseViewHolder<HomeTrainingModel>(binding.root) {

        override fun bind(item: HomeTrainingModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.contentDescription = item.title + IMAGE
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onTrainingBlockClicked()
            }
        }
    }

    inner class PagerViewHolder(private val binding: ViewBlockBinding) :
        BaseViewHolder<HomePagerModel>(binding.root) {

        override fun bind(item: HomePagerModel) {
            binding.blockTitle.text = item.title
            binding.blockImage.contentDescription = item.title + IMAGE
            binding.blockImage.setImageResource(item.iconId)
            itemView.setOnClickListener {
                onBlockListener.onPagerBlockClicked(item.pagerPosition)
            }
        }
    }
}

interface OnBlockListener {
    fun onLinkBlockClicked(link: String)
    fun onAppLinkBlockClicked(title: String, link: Uri)
    fun onPagerBlockClicked(number: Int)
    fun onTrainingBlockClicked()
}

