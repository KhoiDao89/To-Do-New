package vn.htv.fresher.todoapp.presentation.taskdetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.presentation.note.NoteActivity

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

    val taskId = intent.getIntExtra(PARAM_EXTRA_TASK_ID, 0)

    if (taskId == 0) onBackPressed()
    viewModel.taskId = taskId
  }

  override fun initUi() {
    super.initUi()

    showBackButton()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == NoteActivity.PARAM_REQUEST_CODE_TASK_DETAIL_ACTIVITY && resultCode == Activity.RESULT_OK){
      val note = data?.getStringExtra(NoteActivity.PARAM_EXTRA_NOTE)
      viewModel.noteTask(note)
    }
  }

  companion object {
    private const val PARAM_EXTRA_TASK_ID = "TASK_ID"

    fun start(activity: AppCompatActivity, taskId: Int) {
      val intent = Intent(activity, TaskDetailActivity::class.java)

      intent.apply {
        putExtra(PARAM_EXTRA_TASK_ID, taskId)
      }

      activity.startActivity(intent)
    }
  }
}