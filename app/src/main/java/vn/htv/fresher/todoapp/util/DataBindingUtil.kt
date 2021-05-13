package vn.htv.fresher.todoapp.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.util.ext.show

@BindingAdapter("bind:imageResource")
fun setImageResource(imageView: ImageView, @DrawableRes drawableId: Int? = null) {
  val id = drawableId ?: return
  imageView.setImageResource(id)
}

@BindingAdapter("bind:goneUnless")
fun goneUnless(view: View, isShown: Boolean) {
  view.show(isShown)
}

@BindingAdapter("bind:text")
fun setText(textView: TextView, @StringRes resId: Int?) {
  val id = resId ?: return
  textView.setText(id)
}