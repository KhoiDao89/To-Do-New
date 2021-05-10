package vn.htv.fresher.todoapp.presentation.note

import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentNoteBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment

class NoteFragment : BaseFragment<FragmentNoteBinding>() {

  // NoteFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId = R.layout.fragment_note

  private val viewModel by sharedViewModel <NoteViewModel>()

  override fun init() {
    super.init()
    binding.viewModel = viewModel
  }

  companion object {

    /**
     * Create Note instance pattern
     */
    fun newInstance() = NoteFragment()
  }

}