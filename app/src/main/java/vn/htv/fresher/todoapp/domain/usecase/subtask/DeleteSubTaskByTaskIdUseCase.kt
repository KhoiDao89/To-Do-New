package vn.htv.fresher.todoapp.domain.usecase.subtask

import io.reactivex.Completable
import vn.htv.fresher.todoapp.domain.repository.SubTaskRepository

class DeleteSubTaskByTaskIdUseCase(
  private val subTaskRepository: SubTaskRepository
) {
  operator fun invoke(taskId: Int): Completable {
    return subTaskRepository.deleteSubTaskByTaskId(taskId)
  }
}