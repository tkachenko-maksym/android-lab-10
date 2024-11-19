package com.example.lab10.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab10.R
import com.example.lab10.data.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val updateTaskStatus: (Task) -> Unit,
    private val onTaskClicked: (Task) -> Unit
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskName: TextView = itemView.findViewById(R.id.taskName)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        private val taskStatus: CheckBox = itemView.findViewById(R.id.chTaskStatus)

        fun bind(task: Task) {
            taskName.text = task.name
            taskDescription.text = task.description
            taskDate.text = task.prettyDateFormat()
            taskStatus.isChecked = task.isCompleted
            itemView.setOnClickListener { onTaskClicked(task) }
            taskStatus.setOnClickListener {
                task.isCompleted = !task.isCompleted
                updateTaskStatus(task)
                notifyDataSetChanged()
            }
        }
    }
}