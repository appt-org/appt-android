package nl.appt.adapters.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.appt.adapters.BaseViewHolder
import nl.appt.databinding.ViewMeerItemBinding
import nl.appt.model.Block

class SubCategoryAdapter(private val category: ArrayList<Block>, private val onCategoryListener: OnCategoryListener): RecyclerView.Adapter<BaseViewHolder<*>>() {

    inner class ViewHolder(private val binding: ViewMeerItemBinding) :
        BaseViewHolder<Block>(binding.root) {

        override fun bind(item: Block) {
            binding.textView.text = item.title
            GlideApp.with(itemView.context).load(item.image).override(256).into(binding.meerImageView)
            itemView.setOnClickListener {
                onCategoryListener.onCategoryClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = ViewMeerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(category[position])
            else -> throw IllegalArgumentException(BaseViewHolder.ERROR_INVALID_HOLDER + position)
        }
    }

    override fun getItemCount(): Int {
        return category.size
    }
}