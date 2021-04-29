package vn.htv.fresher.todoapp.presentation.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity
import vn.htv.fresher.todoapp.util.ext.replaceFragment

abstract class BaseActivity : AppCompatActivity() {

  @get:LayoutRes
  protected abstract val layoutId: Int

  protected abstract val fragment: Fragment

  // Open method, these method will be implement on child class
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  open fun init() {}

  open fun initUi() {}

  // Activity overridden methods
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutId)

    replaceFragment(fragment)

    init()
    initUi()
  }
}