package vn.htv.fresher.todoapp.presentation.taskdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task_attribute.view.*
import vn.htv.fresher.todoapp.databinding.ItemNextStepBinding
import vn.htv.fresher.todoapp.databinding.ItemNoteBinding
import vn.htv.fresher.todoapp.databinding.ItemSubtaskBinding
import vn.htv.fresher.todoapp.databinding.ItemTaskAttributeBinding
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel

class SubTaskAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val subTaskItemList = mutableListOf<TaskDetailItem>()

  override fun getItemViewType(position: Int): Int {
    return subTaskItemList[position].type.value
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (SubItemType.from(viewType)) {
      SubItemType.SUBTASK_ITEM  -> SubTaskViewHolder(ItemSubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      SubItemType.NEXT_STEP     -> NextStepViewHolder(ItemNextStepBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      SubItemType.ATTRIBUTE     -> TaskAttributeViewHolder(ItemTaskAttributeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      SubItemType.NOTE          -> NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when(val item = subTaskItemList[position]){
      is TaskDetailItem.SubTask -> (holder as? SubTaskViewHolder)?.bind(item.model)
      is TaskDetailItem.TaskAttribute -> (holder as? TaskAttributeViewHolder)?.bind(item.attribute, item.model)
    }
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
    private val bindingSubTask: ItemSubtaskBinding = itemView

    fun bind(model: SubTaskModel) {
      bindingSubTask.model = model
    }
  }

  inner class NextStepViewHolder(itemView: ItemNextStepBinding) : RecyclerView.ViewHolder(itemView.root)

  inner class TaskAttributeViewHolder(itemView: ItemTaskAttributeBinding) : RecyclerView.ViewHolder(itemView.root) {
    private val binding: ItemTaskAttributeBinding = itemView

    fun bind(attribute: TaskAttributeEnum, model: TaskModel) {
      binding.attribute = attribute
      binding.set       = model.getAttributeState(attribute)
      binding.context   = binding.root.context
      binding.model     = model
    }
  }

  inner class NoteViewHolder(itemView: ItemNoteBinding) : RecyclerView.ViewHolder(itemView.root)
}