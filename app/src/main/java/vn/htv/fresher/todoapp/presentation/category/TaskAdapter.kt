package vn.htv.fresher.todoapp.presentation.category

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.TaskItemBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel

class TaskAdapter(
  private val finishedCallback   : ((model: TaskModel) -> Unit),
  private val importantCallback  : ((model: TaskModel) -> Unit),
  private val taskDetailCallback : ((taskId: Int)      -> Unit)
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

  private val taskList = mutableListOf<TaskModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
    val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TaskViewHolder(binding)
  }

  override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
    val task: TaskModel = taskList[position]
    holder.bind(task)
  }

  override fun getItemCount(): Int {
    return taskList.size
  }

  fun setItems(items: List<TaskModel>) {
    taskList.clear()
    taskList.addAll(items)
    notifyDataSetChanged()
  }

  inner class TaskViewHolder(itemView: TaskItemBinding): RecyclerView.ViewHolder(itemView.root) {

    var binding: TaskItemBinding = itemView

    fun bind(model: TaskModel) {
      binding.model = model

      binding.root.setOnClickListener {
        val taskId = model.id ?: return@setOnClickListener

        taskDetailCallback.invoke(taskId)
      }

      binding.root.importantTaskImageView.setImageResource(
        if (model.important) R.drawable.ic_important_blue else R.drawable.ic_important_gray
      )

      binding.root.importantTaskImageView.setOnClickListener {
        importantCallback.invoke(model)
      }

      binding.root.finishStateTaskImageView.setImageResource(
        if (model.finished) R.drawable.ic_finished else R.drawable.ic_not_finish
      )

      binding.root.taskNameTextView.paintFlags = if (model.finished) Paint.STRIKE_THRU_TEXT_FLAG else 0

      binding.root.finishStateTaskImageView.setOnClickListener {
        finishedCallback.invoke(model)
      }
    }
  }
}
