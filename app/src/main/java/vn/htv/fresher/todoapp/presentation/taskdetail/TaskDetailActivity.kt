package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class TaskDetailActivity : BaseActivity() {

  // TaskDetailActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = TaskDetailFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_task_detail

  private val viewModel by viewModel<TaskDetailViewModel>()

  override fun init() {
    super.init()
    val taskId = intent.getIntExtra(PARAM_TASK_ID, 0)
    if (taskId == 0) onBackPressed()
    viewModel.taskId = taskId
  }

  override fun initUi() {
    super.initUi()
    // hiển thị nút back trên toolbar
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = "Khoi Dao"
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if(item.itemId == android.R.id.home ) {
      onBackPressed()
      return true
    }

    return super.onOptionsItemSelected(item)
  }

  companion object {

    private const val PARAM_TASK_ID = "taskId"

    fun start(activity: AppCompatActivity, taskId: Int) {
      val intent = Intent(activity, TaskDetailActivity::class.java)
      intent.putExtra(PARAM_TASK_ID, taskId)
      activity.startActivity(intent)
    }
  }
}