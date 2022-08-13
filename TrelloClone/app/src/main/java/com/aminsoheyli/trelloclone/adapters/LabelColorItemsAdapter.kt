package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.databinding.ItemLabelColorBinding

class LabelColorItemsAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String
) : RecyclerView.Adapter<LabelColorItemsAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(val binding: ItemLabelColorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemLabelColorBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            viewMain.setBackgroundColor(Color.parseColor(item))
            if (item == mSelectedColor)
                ivSelectedColor.visibility = View.VISIBLE
            else
                ivSelectedColor.visibility = View.GONE
            holder.itemView.setOnClickListener {
                if (onItemClickListener != null)
                    onItemClickListener!!.onClick(position, item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onClick(position: Int, color: String)
    }
}