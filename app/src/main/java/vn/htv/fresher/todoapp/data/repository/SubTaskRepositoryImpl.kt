package vn.htv.fresher.todoapp.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.dao.SubTaskDao
import vn.htv.fresher.todoapp.data.db.entity.SubTask
import vn.htv.fresher.todoapp.data.mapper.toModel
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.repository.SubTaskRepository
import vn.htv.fresher.todoapp.util.rx.SchedulerProvider

class SubTaskRepositoryImpl(
  private val schedulerProvider  : SchedulerProvider,
  private val subTaskDao         : SubTaskDao
) : SubTaskRepository {
  override fun deleteSubTask(model: SubTaskModel): Completable {
    val entity = SubTask.fromModel(model)

    return subTaskDao.delete(entity)
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }

  override fun deleteSubTaskList(taskId: Int): Completable {
    return subTaskDao.deleteSubTaskList(taskId)
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }

  override fun get(id: Int): Single<SubTaskModel> {
    return subTaskDao.get(id)
      .map { it.toModel() }
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }

  override fun getSubTaskList(taskId: Int): Single<List<SubTaskModel>> {
    return subTaskDao.getSubTaskList(taskId)
      .map { list -> list.map { it.toModel() } }
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }

  override fun saveSubTask(model: SubTaskModel): Completable {
    val entity = SubTask.fromModel(model)

    return subTaskDao.insert(entity)
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }

  override fun updateSubTask(model: SubTaskModel): Completable {
    val entity = SubTask.fromModel(model)

    return subTaskDao.update(entity)
      .observeOn(schedulerProvider.io())
      .subscribeOn(schedulerProvider.io())
  }
}

