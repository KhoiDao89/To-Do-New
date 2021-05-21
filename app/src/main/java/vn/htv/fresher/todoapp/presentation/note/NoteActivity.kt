package vn.htv.fresher.todoapp.presentation.note

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class NoteActivity : BaseActivity() {

  // NoteActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = NoteFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_note

  private val viewModel by viewModel<NoteViewModel>()

  override fun initUi() {
    super.initUi()

    showBackButton()
    setToolbarTitle(intent.getStringExtra(PARAM_EXTRA_TASK_NAME).toString())

    val note = intent.getStringExtra(PARAM_EXTRA_NOTE)
    viewModel.setNote(note)
  }

  override fun onBackPressed() {
    val note = viewModel.note.value

    val intentBack = Intent().apply {
      putExtra(PARAM_EXTRA_NOTE, note.toString())
    }

    setResult(Activity.RESULT_OK, intentBack)
    finish()
  }

  companion object {
    const val PARAM_EXTRA_NOTE       = "NOTE"
    const val PARAM_EXTRA_TASK_NAME  = "TASK_NAME"

    const val PARAM_REQUEST_CODE_TASK_DETAIL_ACTIVITY  = 100

    fun start(activity: AppCompatActivity, taskName: String, note: String?) {
      val intent = Intent(activity, NoteActivity::class.java)

      intent.apply {
        putExtra(PARAM_EXTRA_TASK_NAME, taskName)
        putExtra(PARAM_EXTRA_NOTE, note)
      }

      activity.startActivityForResult(intent, PARAM_REQUEST_CODE_TASK_DETAIL_ACTIVITY)
    }
  }
}