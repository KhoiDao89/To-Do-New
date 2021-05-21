package vn.htv.fresher.todoapp.domain.usecase.category

import io.reactivex.Single
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.repository.CategoryRepository

class GetCategoryListUseCase(
  private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Single<List<CategoryModel>> {
      return categoryRepository.getCategoryList()
    }
}