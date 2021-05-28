package vn.htv.fresher.todoapp.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.domain.model.CategoryModel

interface CategoryRepository {
  fun deleteCategory(model: CategoryModel): Completable

  fun getCategory(id: Int): Single<CategoryModel>

  fun getCategoryList(): Single<List<CategoryModel>>

  fun saveCategory(model: CategoryModel): Single<Long>

  fun updateCategory(model: CategoryModel): Completable
}