package vn.htv.fresher.todoapp.domain.usecase.task

import io.reactivex.Completable
import vn.htv.fresher.todoapp.domain.repository.TaskRepository

class DeleteTaskListUseCase(
  private val taskRepository: TaskRepository
) {
  operator fun invoke(catId: Int): Completable {
    return taskRepository.deleteTaskList(catId)
  }
}