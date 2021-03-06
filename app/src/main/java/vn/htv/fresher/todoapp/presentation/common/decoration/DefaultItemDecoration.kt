package vn.htv.fresher.todoapp.presentation.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DefaultItemDecoration (
  private val horizontalSpacing : Int,
  private val verticalSpacing   : Int
): RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
    outRect : Rect,
    view    : View,
    parent  : RecyclerView,
    state   : RecyclerView.State
  ) {
    super.getItemOffsets(outRect, view, parent, state)
    outRect.right   = horizontalSpacing
    outRect.left    = horizontalSpacing
    outRect.bottom  = verticalSpacing

    if (parent.getChildLayoutPosition(view) == 0){
      outRect.top = verticalSpacing
    }
  }
}