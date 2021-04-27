package vn.htv.fresher.todoapp.presentation.taskdetail

import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel

class TaskDetailViewModel(
  private val getTaskListUseCase  : GetTaskListUseCase,
  private val saveTaskUseCase     : SaveTaskUseCase
) : BaseViewModel() {

}