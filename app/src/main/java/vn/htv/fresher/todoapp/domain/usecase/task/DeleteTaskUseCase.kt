package vn.htv.fresher.todoapp.domain.usecase.task

import io.reactivex.Completable
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.repository.TaskRepository

class DeleteTaskUseCase(
  private val taskRepository: TaskRepository
) {
  operator fun invoke(model: TaskModel): Completable {
    return taskRepository.deleteTask(model)
  }
}