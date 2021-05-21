package vn.htv.fresher.todoapp.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.htv.fresher.todoapp.databinding.ItemCategoryBinding
import vn.htv.fresher.todoapp.databinding.ItemSeparatorBinding

class CategoryAdapter(
    private val categoryCallback  : ((catId: Int)       -> Unit),
    private val taskGroupCallback : ((group: TaskGroup) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val mainItemList = mutableListOf<MainItem>()

  override fun getItemViewType(position: Int): Int {
    return mainItemList[position].type.value
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
  : RecyclerView.ViewHolder {
    return when(MainItemType.from(viewType)) {
      MainItemType.MAIN_ITEM -> CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      MainItemType.SEPARATOR -> SeparatorViewHolder(ItemSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when(val item = mainItemList[position]) {
      is MainItem.Item -> (holder as? CategoryViewHolder)?.bind(item.model)
    }
  }

  override fun getItemCount(): Int {
    return mainItemList.size
  }

  fun setItems(items: List<MainItem>) {
    mainItemList.clear()
    mainItemList.addAll(items)
    notifyDataSetChanged()
  }

  inner class CategoryViewHolder(itemView: ItemCategoryBinding)
    : RecyclerView.ViewHolder(itemView.root) {
    var binding: ItemCategoryBinding = itemView

    fun bind(model: MainItemModel) {
      binding.model = model

      binding.root.setOnClickListener {
        model.catId?.let      { categoryCallback.invoke(it)   }
        model.taskGroup?.let  { taskGroupCallback.invoke(it)  }
      }
    }
  }

  inner class SeparatorViewHolder(itemView: ItemSeparatorBinding)
    : RecyclerView.ViewHolder(itemView.root)
}