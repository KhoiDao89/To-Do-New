package vn.htv.fresher.todoapp.presentation.taskdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.presentation.main.MainFragment
import vn.htv.fresher.todoapp.presentation.main.MainViewModel

class TaskDetailActivity : BaseActivity() {

  // MainActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = MainFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_task_detail

  private val viewModel by viewModel<MainViewModel>()
}