package com.tayyipgunay.cryptocrazy.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.repository.CryptoRepository
import com.tayyipgunay.cryptocrazy.service.CryptoDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: CryptoRepository, application: Application) :BaseViewModel(application) {

    var cryptoLiveData = MutableLiveData<List<CryptoModel>?>()
    val db = CryptoDataBase(application)
    private val cryptoDao = db.cryptoDao()

    fun loadCrypto(ids: String) {
        launch(Dispatchers.IO) {
          val response = repository.getDetailCrypto("usd", ids)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    response.body()?.let { response ->

                            println(response[0].symbol)
                            println(response[0].currentPrice)
                            println(response[0].image)

                        cryptoLiveData.value=response

                    }
                } else {
                    println(response.code())
                }


            }
        }

    }
}
  /*  fun getCryptoFromRoom(cryptoid: String) {
        launch(Dispatchers.IO) {
            val cryptoisFromRoom = cryptoDao.getCrypto(cryptoid)
            withContext(Dispatchers.Main) {
                cryptoisFromRoom?.let {
                    cryptoLiveData.value = cryptoisFromRoom

                }
            }
        }
    }
}*/










