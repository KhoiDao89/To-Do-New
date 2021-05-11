package vn.htv.fresher.todoapp.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.entity.Category

@Dao
interface CategoryDao {

  @Delete
  fun delete(entity: Category): Completable

  @Insert
  fun insert(entity: Category): Single<Long>

  @Query("SELECT * FROM ${Category.NAME} WHERE ${Category.COLUMN_ID} = :id")
  fun getCategory(id: Int): Single<Category>

  @Query("SELECT * FROM ${Category.NAME}")
  fun getAll(): Single<List<Category>>

  @Update
  fun update(entity: Category): Completable

}
