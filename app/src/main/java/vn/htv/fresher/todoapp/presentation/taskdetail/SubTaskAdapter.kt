package vn.htv.fresher.todoapp.presentation.taskdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_task_detail.view.*
import kotlinx.android.synthetic.main.item_subtask.view.*
import kotlinx.android.synthetic.main.item_task_attribute.view.*
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.ItemNextStepBinding
import vn.htv.fresher.todoapp.databinding.ItemNoteBinding
import vn.htv.fresher.todoapp.databinding.ItemSubtaskBinding
import vn.htv.fresher.todoapp.databinding.ItemTaskAttributeBinding
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel

class SubTaskAdapter(
  private val finishedSubTaskCallback     : ((model: SubTaskModel)  -> Unit),
  private val deleteSubTaskCallback       : ((model: SubTaskModel)  -> Unit),
  private val updateMyDayTaskCallback     : ((model: TaskModel)     -> Unit),
  private val updateReminderTaskCallback  : ((model: TaskModel)     -> Unit),
  private val removeReminderTaskCallback  : ((model: TaskModel)     -> Unit),
  private val updateDeadlineTaskCallback  : ((model: TaskModel)     -> Unit),
  private val removeDeadlineTaskCallback  : ((model: TaskModel)     -> Unit),
  private val updateRepeatTaskCallback    : ((model: TaskModel)     -> Unit),
  private val removeRepeatTaskCallback    : ((model: TaskModel)     -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

      bindingSubTask.root.finisedSubTaskImageView.setOnClickListener {
        finishedSubTaskCallback.invoke(model)
      }
      bindingSubTask.root.deleteSubTaskImageView.setOnClickListener {
        deleteSubTaskCallback.invoke(model)
      }
    }
  }

  inner class NextStepViewHolder(itemView: ItemNextStepBinding) : RecyclerView.ViewHolder(itemView.root)

  inner class TaskAttributeViewHolder(itemView: ItemTaskAttributeBinding) : RecyclerView.ViewHolder(itemView.root) {
    private val bindingAttribute: ItemTaskAttributeBinding = itemView

    fun bind(attribute: TaskAttributeEnum, model: TaskModel) {
      bindingAttribute.attribute = attribute
      bindingAttribute.set       = model.getAttributeState(attribute)
      bindingAttribute.context   = bindingAttribute.root.context
      bindingAttribute.model     = model

      when (attribute) {
        TaskAttributeEnum.MY_DAY -> {
          bindingAttribute.root.setOnClickListener { updateMyDayTaskCallback(model) }
          bindingAttribute.root.removeTaskAttributeImageView.setOnClickListener { updateMyDayTaskCallback(model) }
        }
        TaskAttributeEnum.REMINDER -> {
          bindingAttribute.root.setOnClickListener { updateReminderTaskCallback(model) }
          bindingAttribute.root.removeTaskAttributeImageView.setOnClickListener { removeReminderTaskCallback(model) }
        }
        TaskAttributeEnum.DEADLINE -> {
          bindingAttribute.root.setOnClickListener { updateDeadlineTaskCallback(model) }
          bindingAttribute.root.removeTaskAttributeImageView.setOnClickListener { removeDeadlineTaskCallback(model) }
        }
        TaskAttributeEnum.REPEAT -> {
          bindingAttribute.root.setOnClickListener { updateRepeatTaskCallback(model) }
          bindingAttribute.root.removeTaskAttributeImageView.setOnClickListener { removeRepeatTaskCallback(model) }
        }
      }
    }
  }

  inner class NoteViewHolder(itemView: ItemNoteBinding) : RecyclerView.ViewHolder(itemView.root)
}

@BindingAdapter("bind:textColor")
fun setTextColor(textView: TextView, isSet: Boolean){
  val colorBLue = ContextCompat.getColor(textView.context, R.color.dark_blue)
  val colorGray = ContextCompat.getColor(textView.context, R.color.dark_gray)
  if (isSet) textView.setTextColor(colorBLue) else textView.setTextColor(colorGray)
}

@BindingAdapter("bind:setIconColor")
fun setIconColor(imageView: ImageView, isSet: Boolean){
  val colorBLue = ContextCompat.getColor(imageView.context, R.color.dark_blue)
  val colorGray = ContextCompat.getColor(imageView.context, R.color.dark_gray)
  if (isSet) imageView.setColorFilter(colorBLue) else imageView.setColorFilter(colorGray)
}