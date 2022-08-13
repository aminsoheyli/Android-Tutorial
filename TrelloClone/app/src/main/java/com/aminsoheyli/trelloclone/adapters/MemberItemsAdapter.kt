package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ItemMemberBinding
import com.aminsoheyli.trelloclone.models.User
import com.bumptech.glide.Glide

open class MemberItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<MemberItemsAdapter.ViewHolder>() {

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
        }
    }

    override fun getItemCount(): Int = list.size
}