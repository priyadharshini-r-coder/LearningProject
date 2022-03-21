package com.example.learningproject.protodatastore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learningproject.R
import com.example.learningproject.databinding.ItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder (private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        // Format date as: Apr 6, 2020
        private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

        /**
         * Bind the task to the UI elements
         */
        fun bind(todo: Task) {
            binding.task.text = todo.name
            setTaskPriority(todo)
            binding.deadline.text = dateFormat.format(todo.deadline)
            // if a task was completed, show it grayed out
            val color = if (todo.completed) {
                R.color.greyAlpha
            } else {
                R.color.white
            }
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    color
                )
            )
        }

        @SuppressLint("StringFormatInvalid")
        private fun setTaskPriority(todo: Task) {
            binding.priority.text = itemView.context.resources.getString(
                R.string.priority_value,
                todo.priority.name
            )
            // set the priority color based on the task priority
            val textColor = when (todo.priority) {
                TaskPriority.HIGH -> R.color.red
                TaskPriority.MEDIUM -> R.color.yellow
                TaskPriority.LOW -> R.color.green
            }
            binding.priority.setTextColor(ContextCompat.getColor(itemView.context, textColor))
        }

        companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            val binding =ItemLayoutBinding.bind(view)
            return TaskViewHolder(binding)
        }
    }
    }