package vn.htv.fresher.todoapp.presentation.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel() : ViewModel() {
  protected val disposables = CompositeDisposable()

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }
}