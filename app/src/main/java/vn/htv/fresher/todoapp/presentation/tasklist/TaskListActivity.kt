package vn.htv.fresher.todoapp.presentation.tasklist

import kotlinx.android.synthetic.main.activity_task_list.*
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

abstract class TaskListActivity<VM : TaskListViewModel> : BaseActivity() {
  protected abstract var backgroundResId: Int

  protected abstract val viewModel: VM

  override val layoutId: Int
    get() = R.layout.activity_task_list

  override fun init() {
    viewModel.loadData()
    registerLiveDataListener()
  }

  override fun initUi() {
    showBackButton()
    setBackground()
  }

  open fun setBackground() {
    content.setBackgroundResource(backgroundResId)
  }

  fun registerLiveDataListener() {
    viewModel.title.observe(this,{
      setToolbarTitle(title = it)
    })
  }
}