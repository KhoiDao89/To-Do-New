package vn.htv.fresher.todoapp.domain.usecase.category

import io.reactivex.Single
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.repository.CategoryRepository

class GetCategoryUseCase(
  private val categoryRepository: CategoryRepository
) {
  operator fun invoke(id: Long): Single<CategoryModel> {
    return categoryRepository.getCategory(id.toInt())
  }
}