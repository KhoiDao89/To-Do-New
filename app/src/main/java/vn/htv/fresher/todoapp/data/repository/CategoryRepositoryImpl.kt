package vn.htv.fresher.todoapp.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.dao.CategoryDao
import vn.htv.fresher.todoapp.data.db.entity.Category
import vn.htv.fresher.todoapp.data.mapper.toModel
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.repository.CategoryRepository
import vn.htv.fresher.todoapp.util.rx.SchedulerProvider

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val schedulerProvider: SchedulerProvider
) : CategoryRepository {
    override fun deleteCategory(model: CategoryModel): Completable {
        val entity = Category.fromModel(model)

        return categoryDao.delete(entity)
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.io())
    }

    override fun getCategory(id: Int): Single<CategoryModel> {
        return categoryDao.getCategory(id)
            .map { it.toModel() }
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.io())
    }

    override fun getCategoryList(): Single<List<CategoryModel>> {
        return categoryDao.getAll()
            .map { list -> list.map { it.toModel() } }
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.io())
    }

    override fun saveCategory(model: CategoryModel): Single<Long> {
        val entity = Category.fromModel(model)

        return categoryDao.insert(entity)
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.io())
    }

    override fun updateCategory(model: CategoryModel): Completable {
        val entity = Category.fromModel(model)

        return categoryDao.update(entity)
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.io())
    }
}
