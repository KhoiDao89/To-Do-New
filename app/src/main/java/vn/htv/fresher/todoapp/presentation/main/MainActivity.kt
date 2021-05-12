package vn.htv.fresher.todoapp.presentation.main

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class MainActivity : BaseActivity() {

  // MainActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = MainFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_main

  private val viewModel by viewModel<MainViewModel>()
}