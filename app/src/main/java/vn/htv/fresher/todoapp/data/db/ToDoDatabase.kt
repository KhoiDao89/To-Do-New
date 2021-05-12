package vn.htv.fresher.todoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vn.htv.fresher.todoapp.data.db.converter.DateTimeConverter
import vn.htv.fresher.todoapp.data.db.dao.CategoryDao
import vn.htv.fresher.todoapp.data.db.dao.SubTaskDao
import vn.htv.fresher.todoapp.data.db.dao.TaskDao
import vn.htv.fresher.todoapp.data.db.entity.Category
import vn.htv.fresher.todoapp.data.db.entity.SubTask
import vn.htv.fresher.todoapp.data.db.entity.Task

@Database(
  entities = [
    Category::class,
    SubTask::class,
    Task::class
  ],
  version = 1
)

@TypeConverters(DateTimeConverter::class)
abstract class ToDoDatabase: RoomDatabase() {

  abstract fun categoryDao(): CategoryDao

  abstract fun subTaskDao(): SubTaskDao

  abstract fun taskDao(): TaskDao
}