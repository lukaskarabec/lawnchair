package cz.appkazdarma.aiasistent.presentation.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.domain.model.TaskItem

class TaskAdapter(
    private val onEdit: (TaskItem) -> Unit,
    private val onDelete: (TaskItem) -> Unit
) : ListAdapter<TaskItem, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskItem>() {
            override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem == newItem
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.taskTitle)
        private val priority: TextView = view.findViewById(R.id.taskPriority)
        private val edit: ImageButton = view.findViewById(R.id.editButton)
        private val delete: ImageButton = view.findViewById(R.id.deleteButton)

        fun bind(task: TaskItem) {
            title.text = task.title
            priority.text = task.priority.toString()
            edit.setOnClickListener { onEdit(task) }
            delete.setOnClickListener { onDelete(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
