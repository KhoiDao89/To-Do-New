package vn.htv.fresher.todoapp.presentation.common

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
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

  open fun showBackButton() {
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setDisplayShowHomeEnabled(true)
    }
  }

  open fun setToolbarTitle(title: String) {
    supportActionBar?.apply { this.title = title }
  }

  open fun setToolbarTitleRes(@StringRes title: Int) {
    supportActionBar?.apply { this.title = getString(title) }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if(item.itemId == android.R.id.home ) {
      onBackPressed()
      return true
    }

    return super.onOptionsItemSelected(item)
  }

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