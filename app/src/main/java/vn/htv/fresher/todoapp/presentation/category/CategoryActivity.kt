package vn.htv.fresher.todoapp.presentation.category

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity

class CategoryActivity: BaseActivity() {

  private lateinit var viewmodel: CategoryViewModel
//  private val actionbar = getSupportActionBar()

  override val fragment: Fragment
    get() = CategoryFragment.newInstance()

  override val layoutId: Int
    get() = R.layout.activity_category

  override fun initUi() {
    super.initUi()
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    getMenuInflater().inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.getItemId()) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      else -> {
      }
    }
    return super.onOptionsItemSelected(item)
  }
  companion object {

    fun start(activity: AppCompatActivity) {
      val intent = Intent(activity, CategoryActivity::class.java)
      activity.startActivity(intent)
    }
  }
}