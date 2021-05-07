package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.presentation.note.NoteFragment
import vn.htv.fresher.todoapp.presentation.note.NoteViewModel

class NoteActivity : BaseActivity() {

  // NoteActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = NoteFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_note

  private val viewModel by viewModel<NoteViewModel>()

  override fun init() {
    super.init()
  }

  override fun initUi() {
    super.initUi()

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setDisplayShowHomeEnabled(true)
      title = intent.getStringExtra(PARAM_TASK_NAME)
    }

    val note = intent.getStringExtra(PARAM_NOTE)
    viewModel.setNote(note)
  }



  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if(item.itemId == android.R.id.home ) {
      onBackPressed()
      return true
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    val note = viewModel.note.value

    intent.putExtra(PARAM_NOTE, note.toString())
    setResult(PARAM_RESULT_CODE, intent)

  }

  companion object {

    const val PARAM_REQUEST_CODE  = 100
    const val PARAM_RESULT_CODE   = 100
    const val PARAM_TASK_NAME     = "TASKNAME"
    const val PARAM_NOTE          = "NOTE"

    fun start(activity: AppCompatActivity, taskName: String, note: String?) {
      val intent = Intent(activity, NoteActivity::class.java)

      intent.apply {
        putExtra(PARAM_TASK_NAME, taskName)
        putExtra(PARAM_NOTE, note)
      }

      activity.startActivityForResult(intent, PARAM_REQUEST_CODE)
    }
  }
}