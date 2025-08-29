package cz.appkazdarma.aiasistent.presentation.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.domain.model.TaskItem
import cz.appkazdarma.aiasistent.databinding.ActivityTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(onEdit = { showTaskDialog(it) }, onDelete = { viewModel.deleteTask(it.id) })
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter

        binding.addTaskButton.setOnClickListener { showTaskDialog(null) }

        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }
    }

    private fun showTaskDialog(task: TaskItem?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null)
        val titleField = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.titleField)
        val priorityField = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.priorityField)

        titleField.setText(task?.title ?: "")
        priorityField.setText(task?.priority?.toString() ?: "")

        MaterialAlertDialogBuilder(this)
            .setTitle(if (task == null) R.string.new_task else R.string.edit_task)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { dialog: DialogInterface, _ ->
                val title = titleField.text?.toString() ?: ""
                val prio = priorityField.text?.toString()?.toIntOrNull() ?: 0
                if (task == null) {
                    viewModel.addNewTask(title, prio, null)
                } else {
                    viewModel.updateTask(task.copy(title = title, priority = prio))
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _ -> dialog.dismiss() }
            .show()
    }
}
