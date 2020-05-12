package com.morcinek.omise.core.extensions

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun <T> Fragment.singleChoiceSelector(
    title: Int? = null,
    selectedItem: T? = null,
    items: List<T>,
    toString: T.() -> String,
    onClick: (T) -> Unit
) = singleChoiceSelector(title, selectedItem?.let(toString), items.map(toString)) { _, index -> onClick(items[index]) }

fun Fragment.singleChoiceSelector(
    title: Int? = null,
    selectedItem: CharSequence? = null,
    items: List<CharSequence>,
    onClick: (DialogInterface, Int) -> Unit
) {
    with(AlertDialog.Builder(requireContext())) {
        if (title != null) {
            setTitle(title)
        }
        setSingleChoiceItems(Array(items.size) { i -> items[i].toString() }, items.indexOf(selectedItem)) { dialog, which ->
            onClick(dialog, which)
            dialog.dismiss()
        }
        show()
    }
}