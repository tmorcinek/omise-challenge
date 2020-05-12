package com.morcinek.omise.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class ApiViewModel : ViewModel() {

    val error = MutableLiveData<Throwable>()
    val progress = MutableLiveData(false)

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        error.postValue(throwable)
        progress.postValue(false)
    }

    protected fun <T> requestData(mutableLiveData: MutableLiveData<T>, block: suspend CoroutineScope.() -> T) {
        progress.postValue(true)
        viewModelScope.launch(errorHandler) {
            mutableLiveData.postValue(withContext(Dispatchers.IO, block))
            progress.postValue(false)
        }
    }
}