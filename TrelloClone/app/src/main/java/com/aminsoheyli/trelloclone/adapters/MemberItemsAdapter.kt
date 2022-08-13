package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ItemMemberBinding
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants
import com.bumptech.glide.Glide

open class MemberItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<MemberItemsAdapter.ViewHolder>() {
    private lateinit var onClickListener: OnClickListener

    class ViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        with(holder.binding) {
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(ivMemberImage)
            tvMemberName.text = model.name
            tvMemberEmail.text = model.email
            if (model.selected)
                ivSelectedMember.visibility = View.VISIBLE
            else
                ivSelectedMember.visibility = View.GONE
            holder.itemView.setOnClickListener {
                if (this@MemberItemsAdapter::onClickListener.isInitialized)
                    if (model.selected)
                        onClickListener.onClick(position, model, Constants.UN_SELECT)
                    else
                        onClickListener.onClick(position, model, Constants.SELECT)
            }

        }
    }

    override fun getItemCount(): Int = list.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }
}