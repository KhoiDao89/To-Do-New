package vn.htv.fresher.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.domain.model.CategoryModel

@Entity(tableName = Category.NAME)
data class Category(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = COLUMN_ID)          val id         : Int? = null,
  @ColumnInfo(name = COLUMN_NAME)        val name       : String,
  @ColumnInfo(name = COLUMN_ICON)        val icon       : String? = null,
  @ColumnInfo(name = COLUMN_CREATED_AT)  val createdAt  : LocalDateTime
) {
  companion object {
    const val NAME               = "category"
    const val COLUMN_ID          = "id"
    const val COLUMN_NAME        = "name"
    const val COLUMN_ICON        = "icon"
    const val COLUMN_CREATED_AT  = "created_at"

    fun fromModel(model: CategoryModel) = Category(
      name       = model.name,
      id         = model.id,
      icon       = model.icon,
      createdAt  = model.createdAt
    )
  }
}
