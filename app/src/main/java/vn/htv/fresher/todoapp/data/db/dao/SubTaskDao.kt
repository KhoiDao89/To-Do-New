package vn.htv.fresher.todoapp.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.entity.SubTask

@Dao
interface SubTaskDao {

  @Delete
  fun delete(entity: SubTask): Completable

  @Insert
  fun insert(entity: SubTask): Completable

  @Query("SELECT * FROM ${SubTask.NAME} WHERE ${SubTask.COLUMN_ID} = :id")
  fun get(id: Int): Single<SubTask>

  @Query("SELECT * FROM ${SubTask.NAME}")
  fun getAll(): Single<List<SubTask>>

  @Query("SELECT * FROM ${SubTask.NAME} WHERE ${SubTask.COLUMN_TASK_ID} = :taskId")
  fun getByTaskId(taskId: Int): Single<List<SubTask>>

  @Update
  fun update(entity: SubTask): Completable

}
