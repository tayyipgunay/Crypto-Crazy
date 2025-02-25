package com.tayyipgunay.cryptocrazy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    protected open val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Global Exception Caught: ${throwable.message}")
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + coroutineExceptionHandler

    override fun onCleared() {
        super.onCleared()
        job.cancel() // Tüm coroutine'leri iptal eder
    }
}
