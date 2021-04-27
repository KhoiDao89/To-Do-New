package vn.htv.fresher.todoapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vn.htv.fresher.todoapp.data.repository.CategoryRepositoryImpl
import vn.htv.fresher.todoapp.data.repository.SubTaskRepositoryImpl
import vn.htv.fresher.todoapp.data.repository.TaskRepositoryImpl
import vn.htv.fresher.todoapp.domain.repository.CategoryRepository
import vn.htv.fresher.todoapp.domain.repository.SubTaskRepository
import vn.htv.fresher.todoapp.domain.repository.TaskRepository
import vn.htv.fresher.todoapp.domain.usecase.category.DeleteCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryListUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.SaveCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.UpdateCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.subtask.*
import vn.htv.fresher.todoapp.domain.usecase.task.*
import vn.htv.fresher.todoapp.presentation.category.CategoryViewModel
import vn.htv.fresher.todoapp.presentation.main.MainViewModel
import vn.htv.fresher.todoapp.presentation.note.NoteViewModel
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailViewModel
import vn.htv.fresher.todoapp.util.rx.AppSchedulerProvider
import vn.htv.fresher.todoapp.util.rx.SchedulerProvider

val appModule = module {

  single<SchedulerProvider>(createdAtStart = true) { AppSchedulerProvider() }

  // Repository
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
  single<SubTaskRepository> { SubTaskRepositoryImpl(get(), get()) }
  single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
  // UseCase
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  factory { DeleteCategoryUseCase(get()) }
  factory { GetCategoryListUseCase(get()) }
  factory { SaveCategoryUseCase(get()) }
  factory { UpdateCategoryUseCase(get()) }

  factory { DeleteTaskUseCase(get()) }
  factory { GetTaskListUseCase(get()) }
  factory { GetTaskUseCase(get()) }
  factory { SaveTaskUseCase(get()) }
  factory { UpdateTaskUseCase(get()) }

  factory { DeleteSubTaskUseCase(get()) }
  factory { GetSubTaskUseCase(get())}
  factory { GetSubTaskListUseCase(get()) }
  factory { SaveSubTaskUseCase(get()) }
  factory { UpdateSubTaskUseCase(get()) }


  // ViewModel
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  viewModel { CategoryViewModel(get(), get(), get(), get()) }
  viewModel { MainViewModel(get(), get(), get(), get()) }
  viewModel { NoteViewModel() }
  viewModel { TaskDetailViewModel(get(), get(), get(), get(), get(), get(), get()) }
}