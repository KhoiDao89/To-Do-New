package vn.htv.fresher.todoapp.presentation.taskdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.htv.fresher.todoapp.databinding.ItemSubtaskBinding
import vn.htv.fresher.todoapp.domain.model.SubTaskModel

class SubTaskAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val subTaskItemList = mutableListOf<TaskDetailItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return SubTaskViewHolder(ItemSubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = subTaskItemList[position]
    (holder as? SubTaskViewHolder)?.bind(item.model)
  }

  override fun getItemCount(): Int {
    return subTaskItemList.size
  }

  fun setItems(items: List<TaskDetailItem>){
    subTaskItemList.clear()
    subTaskItemList.addAll(items)
    notifyDataSetChanged()
  }

  inner class SubTaskViewHolder(itemView: ItemSubtaskBinding) : RecyclerView.ViewHolder(itemView.root) {
    val bindingSubTask: ItemSubtaskBinding = itemView

    fun bind(model: SubTaskModel) {
      bindingSubTask.model = model
    }
  }
}