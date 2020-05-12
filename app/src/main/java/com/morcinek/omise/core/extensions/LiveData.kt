package com.morcinek.omise.core.extensions

import androidx.lifecycle.*

inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) = liveData.observe(this, Observer { observer(it) })

inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, observer: Observer<T>) = liveData.observe(this, observer)

fun <X, Y> LiveData<X>.map(mapFunction: (X) -> (Y)): LiveData<Y> = Transformations.map(this, mapFunction)

fun <A, B, C> combine(sourceA: LiveData<A>, sourceB: LiveData<B>, sourceC: LiveData<C>): LiveData<Triple<A, B, C>> = MediatorLiveData<Triple<A, B, C>>().apply {

    var valueA: A? = null
    var valueB: B? = null
    var valueC: C? = null

    fun update() {
        if (valueA != null && valueB != null && valueC != null) {
            value = Triple(valueA!!, valueB!!, valueC!!)
        }
    }
    addSource(sourceA) {
        valueA = it
        update()
    }
    addSource(sourceB) {
        valueB = it
        update()
    }
    addSource(sourceC) {
        valueC = it
        update()
    }
}
