package nl.appt.adapters.category

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import nl.appt.R
import nl.appt.adapters.BaseViewHolder
import nl.appt.databinding.ViewBlockBinding
import nl.appt.model.Block

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
            binding.blockImage.contentDescription = item.title + IMAGE
            GlideApp.with(itemView.context).load(item.image)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(binding.blockImage)
            itemView.setOnClickListener {
                onCategoryListener.onCategoryClicked(item)
            }
        }
    }
}


