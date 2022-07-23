package com.aminsoheyli.a7minutesworkout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<Exercise>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Exercise, context: Context) {
            binding.textViewName.text = item.id.toString()
            when (item.status) {
                Exercise.Status.SELECTED -> {
                    binding.textViewName.background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.item_circular_thin_color_accent_border
                        )
                    binding.textViewName.setTextColor(Color.parseColor("#212121"))
                }
                Exercise.Status.COMPLETED -> {
                    binding.textViewName.background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.item_circular_color_accent_background
                        )
                    binding.textViewName.setTextColor(Color.parseColor("#FFFFFF"))
                }
                Exercise.Status.NONE -> {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemExerciseStatusBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(items[position], holder.itemView.context)


    override fun getItemCount(): Int = items.size
}