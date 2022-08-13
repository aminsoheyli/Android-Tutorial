package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ItemCardSelectedMemberBinding
import com.aminsoheyli.trelloclone.models.SelectedMembers
import com.bumptech.glide.Glide

open class CardMemberItemsAdapter(
    private val context: Context,
    private var list: ArrayList<SelectedMembers>
) : RecyclerView.Adapter<CardMemberItemsAdapter.ViewHolder>() {
    private lateinit var onClickListener: OnClickListener

    class ViewHolder(val binding: ItemCardSelectedMemberBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardSelectedMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if (position == list.size - 1) {
            holder.binding.ivAddMember.visibility = View.VISIBLE
            holder.binding.ivSelectedMemberImage.visibility = View.GONE
        } else {
            holder.binding.ivAddMember.visibility = View.GONE
            holder.binding.ivSelectedMemberImage.visibility = View.VISIBLE
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.binding.ivSelectedMemberImage)
        }
        holder.itemView.setOnClickListener {
            if (this@CardMemberItemsAdapter::onClickListener.isInitialized)
                onClickListener.onClick()
        }
    }

    override fun getItemCount(): Int = list.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick()
    }
}