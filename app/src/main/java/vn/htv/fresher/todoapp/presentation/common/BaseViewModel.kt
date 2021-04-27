package vn.htv.fresher.todoapp.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel

open class BaseViewModel() : ViewModel() {
  protected val disposables = CompositeDisposable()

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }
}