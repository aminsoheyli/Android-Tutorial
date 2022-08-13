package com.aminsoheyli.trelloclone.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.trelloclone.activities.TaskListActivity
import com.aminsoheyli.trelloclone.databinding.ItemTaskBinding
import com.aminsoheyli.trelloclone.models.Task
import java.util.*

open class TaskItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>
) : RecyclerView.Adapter<TaskItemsAdapter.ViewHolder>() {
    private var positionDraggedFrom = -1
    private var positionDraggedTo = -1

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
        val position = holder.adapterPosition
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
            binding.ibEditListName.setOnClickListener {
                binding.llTitleView.visibility = View.GONE
                binding.etEditTaskListName.setText(model.title)
                binding.cvEditTaskListName.visibility = View.VISIBLE
            }
            binding.ibCloseEditableView.setOnClickListener {
                binding.llTitleView.visibility = View.VISIBLE
                binding.cvEditTaskListName.visibility = View.GONE
            }
            binding.ibDoneEditListName.setOnClickListener {
                val listName = binding.etEditTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListActivity)
                        context.updateTaskList(position, listName, model)
                } else
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
            }
            binding.ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(
                    position,
                    model.title
                )
            }
            binding.tvAddCard.setOnClickListener {
                binding.tvAddCard.visibility = View.GONE
                binding.cvAddCard.visibility = View.VISIBLE
            }
            binding.ibCloseCardName.setOnClickListener {
                binding.tvAddCard.visibility = View.VISIBLE
                binding.cvAddCard.visibility = View.GONE
            }
            binding.ibDoneCardName.setOnClickListener {
                val cardName = binding.etCardName.text.toString()
                if (cardName.isEmpty())
                    Toast.makeText(context, "Please Enter a Card Name.", Toast.LENGTH_SHORT).show()
                else if (context is TaskListActivity)
                    context.addCardToTaskList(cardName, position)
            }
            binding.rvCardList.layoutManager = LinearLayoutManager(context)
            binding.rvCardList.setHasFixedSize(true)
            val adapter = CardItemsAdapter(context, model.cards)
            binding.rvCardList.adapter = adapter
            adapter.setOnClickListener(object : CardItemsAdapter.OnClickListener {
                override fun onClick(cardPosition: Int) {
                    if (context is TaskListActivity)
                        context.cardDetails(position, cardPosition)
                }
            })
            val dividerItemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            holder.binding.rvCardList.addItemDecoration(dividerItemDecoration)
            val helper = ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val draggedPosition = viewHolder.adapterPosition
                    val targetPosition = target.adapterPosition
                    if (positionDraggedFrom == -1)
                        positionDraggedFrom = draggedPosition
                    positionDraggedTo = targetPosition
                    Collections.swap(list[position].cards, draggedPosition, targetPosition)
                    adapter.notifyItemMoved(draggedPosition, targetPosition)
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    if (positionDraggedFrom != -1 && positionDraggedTo != -1 && positionDraggedFrom != positionDraggedTo)
                        (context as TaskListActivity).updateCardsInTaskList(
                            position,
                            list[position].cards
                        )
                    positionDraggedFrom = -1
                    positionDraggedTo = -1
                }
            })
            helper.attachToRecyclerView(holder.binding.rvCardList)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
            if (context is TaskListActivity)
                context.deleteTaskList(position)
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()
}