package vn.htv.fresher.todoapp.presentation.note

import androidx.lifecycle.MutableLiveData
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel

class NoteViewModel: BaseViewModel() {

  val note = MutableLiveData<String>()

  fun setNote(note: String?){
    this.note.value = note
  }
}