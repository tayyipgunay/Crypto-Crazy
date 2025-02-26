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
class DetailViewModel @Inject constructor(
    private val repository: CryptoRepository, // CryptoRepository, veri kaynağına erişim sağlar.
    application: Application // Application context'i, veritabanı işlemleri için kullanılır.
) : BaseViewModel(application) { // BaseViewModel'den türetilerek coroutine yönetimi sağlanır.

    // Kripto verilerini tutacak LiveData. Veriler alındığında UI otomatik olarak güncellenir.
    var cryptoLiveData = MutableLiveData<List<CryptoModel>?>()



    // Belirli bir kripto para biriminin detaylarını yüklemek için kullanılan fonksiyon.
    fun loadCrypto(ids: String) {
        // Coroutine başlat ve API çağrısını yap.
        launch(Dispatchers.IO) {
            // API'den belirli bir kripto para biriminin detaylarını al.
            val response = repository.getDetailCrypto("usd", ids)

            // Ana thread'e geç ve sonuçları işle.
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) { // API çağrısı başarılı ise.
                    response.body()?.let { response ->
                        // Alınan verileri logla.
                        println(response[0].symbol) // Kripto para biriminin sembolü.
                        println(response[0].currentPrice) // Kripto para biriminin güncel fiyatı.
                        println(response[0].image) // Kripto para biriminin resim URL'si.

                        // LiveData'yı güncelle ve UI'ı tetikle.
                        cryptoLiveData.value = response
                    }
                } else { // API çağrısı başarısız ise.
                    println("API çağrısı başarısız. Hata kodu: ${response.code()}")
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










