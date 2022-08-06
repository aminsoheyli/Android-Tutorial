package com.projemanag.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.activities.TaskListActivity
import com.aminsoheyli.trelloclone.databinding.ItemTaskBinding
import com.aminsoheyli.trelloclone.models.Task

open class TaskItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>
) : RecyclerView.Adapter<TaskItemsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val model = list[position]
            if (position == list.size - 1) {
                binding.llTaskItem.visibility = View.GONE
                binding.tvAddTaskList.visibility = View.VISIBLE
            } else {
                binding.tvAddTaskList.visibility = View.GONE
                binding.llTaskItem.visibility = View.VISIBLE
            }
            binding.tvTaskListTitle.text = model.title
            binding.tvAddTaskList.setOnClickListener {
                binding.tvAddTaskList.visibility = View.GONE
                binding.cvAddTaskListName.visibility = View.VISIBLE
            }
            binding.ibCloseListName.setOnClickListener {
                binding.tvAddTaskList.visibility = View.VISIBLE
                binding.cvAddTaskListName.visibility = View.GONE
            }
            binding.ibDoneListName.setOnClickListener {
                val listName = binding.etTaskListName.text.toString()
                if (listName.isEmpty())
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                else if (context is TaskListActivity)
                    context.createTaskList(listName)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()
}