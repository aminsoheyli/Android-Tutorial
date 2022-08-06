package com.projemanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ItemBoardBinding
import com.aminsoheyli.trelloclone.models.Board
import com.bumptech.glide.Glide

open class BoardItemsAdapter(
    private val context: Context, private var list: ArrayList<Board>
) : RecyclerView.Adapter<BoardItemsAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val model = list[position]
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(binding.ivBoardImage)
            binding.tvName.text = model.name
            binding.tvCreatedBy.text = "Created By : ${model.createdBy}"
            itemView.setOnClickListener {
                if (onClickListener != null)
                    onClickListener!!.onClick(position, model)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int = list.size

    interface OnClickListener {
        fun onClick(position: Int, model: Board)
    }
}