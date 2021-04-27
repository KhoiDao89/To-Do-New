package vn.htv.fresher.todoapp.presentation.taskdetail

import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentMainBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.main.MainViewModel

class TaskDetailFragment : BaseFragment<FragmentMainBinding>() {

  // MainFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId: Int
    get() = R.layout.fragment_main

  private val viewModel by viewModel<MainViewModel>()

  override fun init() {
    super.init()

  }

  override fun initUi() {
    super.initUi()

    fab.setOnClickListener {
    }
  }

  /**
   * Static definition
   */
  companion object {

    /**
     * Create MainFragment instance pattern
     */
    fun newInstance() = TaskDetailFragment()
  }
}