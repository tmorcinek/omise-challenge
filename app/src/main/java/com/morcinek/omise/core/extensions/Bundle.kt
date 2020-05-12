package com.morcinek.omise.core.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

fun Parcelable.toBundle() = Bundle().apply { putParcel(this@toBundle) }

fun <T : Parcelable> Bundle.putParcel(value: T, key: String = value.javaClass.name) = putParcelable(key, value)

inline fun <reified T : Parcelable> Fragment.getParcelable(key: String = T::class.java.name): T = arguments?.getParcel(key)!!

inline fun <reified T : Parcelable> Bundle.getParcel(key: String = T::class.java.name): T? = getParcelable(key)
