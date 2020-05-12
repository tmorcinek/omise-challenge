package com.morcinek.omise.core

import android.app.Dialog
import android.content.Context
import androidx.lifecycle.Observer
import com.morcinek.omise.R

class BlockingDialog(private val context: Context) : Observer<Boolean> {

    private var dialog: Dialog? = null

    override fun onChanged(t: Boolean) {
        if (t) {
            dialog?.dismiss()
            dialog = Dialog(context, R.style.DimmedDialogTheme).apply {
                setCancelable(false)
                show()
            }
        } else {
            dialog?.dismiss()
        }
    }
}
