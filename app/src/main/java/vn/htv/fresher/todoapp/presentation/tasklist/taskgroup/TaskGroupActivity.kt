package vn.htv.fresher.todoapp.presentation.tasklist.taskgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.presentation.main.TaskGroup
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListActivity

class TaskGroupActivity : TaskListActivity<TaskGroupViewModel>() {
  override val backgroundResId: Int
    get() = viewModel.taskGroup?.backgroundResId ?: throw NullPointerException("backgroundResId has not value")

  override val fragment: Fragment
    get() = TaskGroupFragment.newInstance()

  override val viewModel by viewModel<TaskGroupViewModel>()

  override fun init() {
    val group = TaskGroup.from(intent.getStringExtra(PARAM_EXTRA_TASK_GROUP))
    viewModel.taskGroup = group

    super.init()
  }

  companion object {
    private const val PARAM_EXTRA_TASK_GROUP = "param_extra_task_group"

    fun start(activity: AppCompatActivity, group: TaskGroup) {
      val intent = Intent(activity, TaskGroupActivity::class.java).apply {
        putExtra(PARAM_EXTRA_TASK_GROUP, group.name)
      }

      activity.startActivity(intent)
    }
  }
}