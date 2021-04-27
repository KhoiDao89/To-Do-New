package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentCategoryBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

  //val mesage: String? = getArguments()?.getString("category")

  override val layoutId: Int
    get() = R.layout.fragment_category

  private val viewModel by viewModel<CategoryViewModel>()

  private val taskAdapter by lazy {
    TaskAdapter()
  }

  override fun init() {
    super.init()
    viewModel.insertTask()
    viewModel.insertTask()
    viewModel.insertTask()
//    viewModel.insertCategory()
    viewModel.loadData()
  }
  override fun initUi() {
    super.initUi()
//    cat.apply {
//      text = mesage
//    }
    recycleView.apply {
      adapter = taskAdapter
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.itemList.observe(this, Observer {
      taskAdapter.setItems(it)
    })
  }

  /**
   * Static definition
   */
  companion object {

    /**
     * Create MainFragment instance pattern
     */
    fun newInstance() = CategoryFragment()
  }
}