package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class TaskDetailActivity : BaseActivity() {

  // MainActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = TaskDetailFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_task_detail

  private val viewModel by viewModel<TaskDetailViewModel>()

  override fun initUi() {
    super.initUi()
    // hiển thị nút back trên toolbar
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = "Khoi Dao"
  }

  // Quay lại activity cấp 1
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  companion object {
    fun start(activity: AppCompatActivity) {
      val intent = Intent(activity, TaskDetailActivity::class.java)
      activity.startActivity(intent)
    }
  }
}