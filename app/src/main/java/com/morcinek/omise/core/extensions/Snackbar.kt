package com.morcinek.omise.core.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.longSnackbar(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()