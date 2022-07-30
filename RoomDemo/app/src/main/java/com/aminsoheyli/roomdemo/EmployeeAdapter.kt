package com.aminsoheyli.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.roomdemo.database.EmployeeEntity
import com.aminsoheyli.roomdemo.databinding.ItemEmployeeBinding

class EmployeeAdapter(
    private val items: ArrayList<EmployeeEntity>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemEmployeeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        val color = if (position % 2 == 0) R.color.colorLightGray else R.color.white
        holder.llMain.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context, color
            )
        )

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int = items.size
}