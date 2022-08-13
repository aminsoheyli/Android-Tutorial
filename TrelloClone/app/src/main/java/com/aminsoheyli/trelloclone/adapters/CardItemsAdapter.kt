package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.activities.TaskListActivity
import com.aminsoheyli.trelloclone.databinding.ItemCardBinding
import com.aminsoheyli.trelloclone.models.Card
import com.aminsoheyli.trelloclone.models.SelectedMembers

open class CardItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<CardItemsAdapter.ViewHolder>() {
    private lateinit var onClickListener: OnClickListener

    class ViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val position = holder.adapterPosition
        val model = list[position]

        if (model.labelColor.isNotEmpty()) {
            holder.binding.viewLabelColor.visibility = View.VISIBLE
            holder.binding.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
        } else
            holder.binding.viewLabelColor.visibility = View.GONE

        holder.binding.tvCardName.text = model.name
        if ((context as TaskListActivity).assignedMembersDetailList.size > 0) {
            val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()
            for (i in context.assignedMembersDetailList.indices)
                for (j in model.assignedTo)
                    if (context.assignedMembersDetailList[i].id == j) {
                        val selectedMember = SelectedMembers(
                            context.assignedMembersDetailList[i].id,
                            context.assignedMembersDetailList[i].image
                        )
                        selectedMembersList.add(selectedMember)
                    }

            if (selectedMembersList.size > 0) {
                if (selectedMembersList.size == 1 && selectedMembersList[0].id == model.createdBy)
                    holder.binding.rvCardSelectedMembersList.visibility = View.GONE
                else {
                    holder.binding.rvCardSelectedMembersList.visibility = View.VISIBLE
                    holder.binding.rvCardSelectedMembersList.layoutManager =
                        GridLayoutManager(context, 4)
                    val adapter = CardMemberItemsAdapter(context, selectedMembersList, false)
                    holder.binding.rvCardSelectedMembersList.adapter = adapter
                    adapter.setOnClickListener(object : CardMemberItemsAdapter.OnClickListener {
                        override fun onClick() {
                            if (this@CardItemsAdapter::onClickListener.isInitialized)
                                onClickListener.onClick(position)
                        }
                    })
                }
            } else
                holder.binding.rvCardSelectedMembersList.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (this@CardItemsAdapter::onClickListener.isInitialized)
                onClickListener.onClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(cardPosition: Int)
    }
}