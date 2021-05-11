package vn.htv.fresher.todoapp.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.entity.Task
import java.util.*

@Dao
interface TaskDao {

  @Delete
  fun delete(entity: Task): Completable

  @Query("SELECT * FROM ${Task.NAME} WHERE ${Task.COLUMN_ID} = :id")
  fun get(id: Int): Single<Task>

  @Query("SELECT * FROM ${Task.NAME}")
  fun getAll(): Single<List<Task>>

  @Query("SELECT * FROM ${Task.NAME} WHERE ${Task.COLUMN_CAT_ID} = :catId")
  fun getByCatId(catId: Int): Single<List<Task>>

  @Insert
  fun insert(entity: Task): Completable

  @Update
  fun update(entity: Task): Completable
}
