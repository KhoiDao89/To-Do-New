package vn.htv.fresher.todoapp.presentation.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.htv.fresher.todoapp.databinding.TaskItemBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel

class TaskAdapter(): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
  private val taskList = mutableListOf<TaskModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
      val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return TaskViewHolder(binding)
    }

  override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
    val task: TaskModel = taskList.get(position)
    holder.binding.taskModel = task
  }

  override fun getItemCount(): Int {
    return taskList.size
  }

  fun setItems(items: List<TaskModel>) {
    taskList.clear()
    taskList.addAll(items)
    notifyDataSetChanged()
  }
  class TaskViewHolder(itemView: TaskItemBinding): RecyclerView.ViewHolder(itemView.root) {
    val binding: TaskItemBinding
    init {
      binding = itemView
    }
  }
}