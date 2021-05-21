package vn.htv.fresher.todoapp.util.ext

import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import vn.htv.fresher.todoapp.R
import java.util.*

fun Fragment.showConfirmDialog(
  title             : Int? = null,
  message           : String,
  positiveName      : Int,
  negativeName      : Int = R.string.cancel,
  positiveCallback  : (() -> Unit)
) {
  val ctx = context ?: return

  MaterialDialog(ctx).show {
    title(title)
    message(text = message)
    positiveButton(positiveName) { positiveCallback.invoke() }
    negativeButton(negativeName)
  }
}

fun Fragment.showDateTimeDialog(
  title             : Int?,
  dateTimeCallback  : ((dateTime: Calendar) -> Unit)
) {
  val ctx = context ?: return

  MaterialDialog(ctx).show {
    title(res = title)
    dateTimePicker(requireFutureDateTime = true) { _, dateTime ->
      dateTimeCallback(dateTime)
    }
  }
}

fun Fragment.showDateDialog(dateCallback: ((date: Calendar) -> Unit)) {
  val ctx = context ?: return

  MaterialDialog(ctx).show {
    datePicker { _, date ->
      dateCallback.invoke(date)
    }
  }
}

fun Fragment.showListDialog(
  listItems         : Int,
  positionCallback  : ((position: Int) -> Unit)
) {
  val ctx = context ?: return

  MaterialDialog(ctx).show {
    listItems(listItems) { _, index, _ ->
      positionCallback(index)
    }
  }
}

fun Fragment.showInputDialog(
  title         : Int? = null,
  hint          : Int? = null,
  text          : String? = null,
  positiveName  : Int,
  negativeName  : Int? = R.string.cancel,
  saveCallback  : ((name: String) -> Unit)
) {
  val ctx = context ?: return

  MaterialDialog(ctx).show {
    title(title)
    input(
      hintRes = hint,
      prefill = text
    ) { _, content ->
      saveCallback.invoke(content.toString())
    }
    positiveButton(positiveName)
    negativeButton(negativeName)
  }
}