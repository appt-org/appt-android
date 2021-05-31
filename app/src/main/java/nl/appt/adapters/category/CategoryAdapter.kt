package nl.appt.adapters.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.appt.adapters.BaseViewHolder
import nl.appt.databinding.ViewBlockBinding
import nl.appt.model.Block

private const val TEST_IMAGE_SIZE = 256

class CategoryAdapter(
    private val onCategoryListener: OnCategoryListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val category = arrayListOf<Block>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewBlockBinding.inflate(
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

    inner class ViewHolder(private val binding: ViewBlockBinding) :
        BaseViewHolder<Block>(binding.root) {

        override fun bind(item: Block) {
            binding.blockTitle.text = item.title
            GlideApp.with(itemView.context).load(item.image).override(TEST_IMAGE_SIZE)
                .into(binding.blockImage)
            itemView.setOnClickListener {
                onCategoryListener.onCategoryClicked(item)
            }
        }
    }
}


