package com.tayyipgunay.cryptocrazy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// BaseViewModel, tüm ViewModel'ler için temel bir sınıf olarak tasarlanmıştır.
// Bu sınıf, AndroidViewModel'den türetilir ve CoroutineScope'u uygular.
// Bu sayede, tüm ViewModel'ler coroutine'leri yönetebilir.
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    // Coroutine'lerin yaşam döngüsünü yönetmek için bir Job nesnesi oluşturuyoruz.
    // Bu Job, ViewModel temizlendiğinde (onCleared) iptal edilir.
    private val job = Job()

    // Coroutine'lerde oluşabilecek istisnaları (exceptions) yakalamak için bir hata işleyici tanımlıyoruz.
    // Bu, coroutine'lerdeki hataları global olarak yakalar ve loglar.
    protected open val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Global Exception Caught: ${throwable.message}")
    }

    // CoroutineScope'un coroutineContext'ini tanımlıyoruz.
    // Bu context, Main dispatcher'ı kullanır ve hata işleyiciyi (coroutineExceptionHandler) içerir.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + coroutineExceptionHandler

    // ViewModel temizlendiğinde (örneğin, Fragment veya Activity destroy edildiğinde) çağrılır.
    // Bu metod, tüm coroutine'leri iptal eder ve kaynakları serbest bırakır.
    override fun onCleared() {
        super.onCleared()
        job.cancel() // Tüm coroutine'leri iptal eder.
    }
}