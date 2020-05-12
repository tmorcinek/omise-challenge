package com.morcinek.omise.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.Fragment


inline fun <reified T : Activity> Context.createIntent(function: Intent.() -> Unit = {}) = Intent(this, T::class.java).apply(function)

inline fun <reified T : Activity> Activity.startNewActivityFinishCurrent() {
    startActivity<T>()
    finish()
}

inline fun <reified T : Activity> Context.startActivity(function: Intent.() -> Unit = {}) = startActivity(createIntent<T>(function))

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int = 0, function: Intent.() -> Unit = {}) = startActivityForResult(requireContext().createIntent<T>(function), requestCode)
