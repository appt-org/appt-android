package nl.appt.adapters.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.appt.adapters.BaseViewHolder
import nl.appt.databinding.ViewMeerItemBinding
import nl.appt.model.Block

private const val TEST_IMAGE_SIZE = 256

class SubCategoryAdapter(private val onCategoryListener: OnCategoryListener) :
    RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private val category = ArrayList<Block>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewMeerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(category[position])
    }

    override fun getItemCount(): Int {
        return category.size
    }

    fun setData(block: ArrayList<Block>) {
        category.addAll(block)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ViewMeerItemBinding) :
        BaseViewHolder<Block>(binding.root) {

        override fun bind(item: Block) {
            binding.textView.text = item.title
            binding.meerImageView.contentDescription = item.title + IMAGE
            GlideApp.with(itemView.context).load(item.image)
                .into(binding.meerImageView)
            itemView.setOnClickListener {
                onCategoryListener.onCategoryClicked(item)
            }
        }
    }
}