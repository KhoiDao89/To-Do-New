package vn.htv.fresher.todoapp.presentation.category

import android.content.Intent
import androidx.fragment.app.Fragment
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.repository.CategoryRepository
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryListUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class CategoryActivity: BaseActivity() {

  private lateinit var viewmodel: CategoryViewModel
  override val fragment: Fragment
    get() = CategoryFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_category

}