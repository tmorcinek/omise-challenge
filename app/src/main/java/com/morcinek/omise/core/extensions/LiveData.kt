package com.morcinek.omise.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) = liveData.observe(this, Observer { observer(it) })

inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, observer: Observer<T>) = liveData.observe(this, observer)

