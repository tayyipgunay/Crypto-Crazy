package com.tayyipgunay.cryptocrazy.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.repository.CryptoRepository
import com.tayyipgunay.cryptocrazy.service.CryptoDataBase
import com.tayyipgunay.cryptocrazy.util.CustomSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository, // CryptoRepository, veri kaynağına erişim sağlar.
    application: Application // Application context'i, SharedPreferences ve veritabanı işlemleri için kullanılır.
) : BaseViewModel(application) { // BaseViewModel'den türetilerek coroutine yönetimi sağlanır.

    // Kripto verilerini tutacak LiveData. Veriler alındığında UI otomatik olarak güncellenir.
    var cryptoList = MutableLiveData<List<CryptoModel>?>()

    // Hata durumunu tutacak LiveData. API çağrısı başarısız olduğunda true olur.
    var error = MutableLiveData<Boolean>()

    // Yükleme durumunu tutacak LiveData. Veriler yüklenirken true olur.
    val loading = MutableLiveData<Boolean>()

    // Orijinal kripto listesini tutacak LiveData. Arama işlemlerinde kullanılır.
    var initialCrypto = MutableLiveData<List<CryptoModel>>()

    // SharedPreferences, kullanıcı tercihlerini veya önbellek sürelerini saklamak için kullanılır.
    val sharedPreferences = CustomSharedPreferences(application)

    // Veritabanı ve DAO (Data Access Object) nesneleri.
    val db = CryptoDataBase(application)
    private val cryptoDao = db.cryptoDao()

    // Verilerin ne sıklıkla yenileneceğini belirleyen süre (10 dakika).
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L // 10 dakika (nanosecond cinsinden).

    // ViewModel oluşturulduğunda kripto verilerini yükler.
    init {
        LoadCrypto()
    }

    // Kripto verilerini yüklemek için çağrılan fonksiyon.
    fun LoadCrypto() {
        getLoadCryptoApi()
    }

    // Kripto verilerini API'den yükler.
    fun getLoadCryptoApi() {
        // API'den sorgulanacak kripto para birimlerinin listesi.
        val cryptoies = listOf(
            "bitcoin",   // BTC
            "ethereum",  // ETH
            "ripple",    // XRP
            "litecoin",  // LTC
            "solana",    // SOL
            "cardano",   // ADA
            "polkadot",  // DOT
            "binancecoin" // BNB
        )

        // Yükleme durumunu başlat ve hata durumunu sıfırla.
        loading.value = true
        error.value = false

        // Kripto para birimlerinin ID'lerini virgülle birleştirerek API'ye gönderilecek hale getir.
        val ids = cryptoies.joinToString(",")

        // Coroutine başlat ve API çağrısını yap.
        launch(Dispatchers.IO) {
            val response = repository.getCrypto("usd", ids) // API'den verileri al.

            // Ana thread'e geç ve sonuçları işle.
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) { // API çağrısı başarılı ise.
                    response.body()?.let { response ->
                        println("API çağrısı başarılı.")
                        // Alınan verileri işle ve UI'ı güncelle.
                        showCryptos(response)
                    }
                } else { // API çağrısı başarısız ise.
                    error.value = true
                    loading.value = false
                    println("API çağrısı başarısız. Hata kodu: ${response.code()}")
                }
            }
        }
    }

    // Kripto verilerini UI'da göstermek için kullanılan fonksiyon.
    fun showCryptos(Crypolist: List<CryptoModel>) {
        cryptoList.value = Crypolist // LiveData'yı güncelle.
        loading.value = false // Yükleme durumunu durdur.
        initialCrypto.value = Crypolist // Orijinal listeyi sakla.
        error.value = false // Hata durumunu sıfırla.
    }

    // Kripto para birimlerini aramak için kullanılan fonksiyon.
    fun searchCrypto(query: String?) {
        loading.value = true // Yükleme durumunu başlat.

        // Orijinal kripto listesini al.
        val listToSearch = initialCrypto.value

        // Coroutine başlat ve arama işlemini yap.
        launch(Dispatchers.Default) {
            // Arama sorgusuna göre filtreleme yap.
            val results = listToSearch?.filter {
                it.symbol.contains(query?.trim() ?: "", ignoreCase = true)
            }

            // Ana thread'e geç ve sonuçları UI'da göster.
            withContext(Dispatchers.Main) {
                loading.value = false // Yükleme durumunu durdur.
                cryptoList.value = results // Filtrelenmiş listeyi LiveData'ya atar.
                println("Arama işlemi tamamlandı ve sonuçlar güncellendi.")
            }
        }
    }
}