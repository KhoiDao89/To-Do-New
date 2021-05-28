package vn.htv.fresher.todoapp.domain.usecase.subtask

import io.reactivex.Single
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.repository.SubTaskRepository

class GetSubTaskUseCase(
  private val subTaskRepository: SubTaskRepository
) {
  operator fun invoke(id: Int): Single<SubTaskModel> {
    return subTaskRepository.get(id)
  }
}