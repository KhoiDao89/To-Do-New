package vn.htv.fresher.todoapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.category.CategoryActivity
import vn.htv.fresher.todoapp.presentation.category.CategoryFragment
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class MainActivity : BaseActivity() {

  // MainActivity class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val fragment: Fragment
    get() = MainFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_main

  private val viewModel by viewModel<MainViewModel>()

  override fun initUi() {
    super.initUi()
    button1.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
//        val bundle = Bundle()
//        val model = TaskModel(
//            catId = 1,
//            name = "",
//            finished = true,
//            deadline = LocalDateTime.now(),
//            myDay = true,
//            important = true,
//            reminder = LocalDateTime.now(),
//            repeat = 1,
//            createdAt = LocalDateTime.now(),
//            note = ""
//        )
//        viewModel.saveCategory(model)

//        bundle.putString("category",button1.text.toString())
//        val fragobj = CategoryFragment()
//        fragobj.setArguments(bundle)
        val intent = Intent(this@MainActivity, CategoryActivity::class.java)
        startActivity(intent)
      }
    })

  }
}