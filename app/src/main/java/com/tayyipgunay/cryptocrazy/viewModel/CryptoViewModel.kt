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
class CryptoViewModel
@Inject constructor( private val repository: CryptoRepository,application: Application) : BaseViewModel(application) {

   var cryptoList = MutableLiveData<List<CryptoModel>?>()
    // Crypto verilerini tutacak LiveData, veriler alındığında UI otomatik olarak güncellenir
    var error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    var initialCrypto = MutableLiveData<List<CryptoModel>>()
    val sharedPreferences = CustomSharedPreferences(application)

    val db = CryptoDataBase(application)
    private val cryptoDao = db.cryptoDao()
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L//10 dakika
    //10 dakikayı nanosecond cinsinden 600,000,000,000 nanosecond olarak temsil eder.

//refreshLayout olacak
    //gerekli metotlar yapılacak
    //CryptoDao  olusturulacak
    //


    //var isUsingOriginalList=MutableLiveData<Boolean>()
    fun LoadCrypto() {
       /* val updateTime = sharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {//
//
            getFromSqlLite()
            Toast.makeText(getApplication(), "getDataFromSqlLite metodu çalıştı", Toast.LENGTH_LONG).show()




        }
        else{
            getLoadCryptoApi()
            Toast.makeText(getApplication(), "getDataFromApi metodu çalıştı", Toast.LENGTH_LONG).show()
}*/
        getLoadCryptoApi()
    }

    init {
        LoadCrypto()
    }


    fun searchCrypto(query: String?) {
        loading.value=true

        val listToSearch = initialCrypto.value

        launch(Dispatchers.Default) {
            val results =
                listToSearch?.filter { it.symbol.contains(query?.trim() ?: "", ignoreCase = true) }
            withContext(Dispatchers.Main) {
                loading.value=false
               cryptoList.value = results//
                println("search crypto metodu tetkilendi ve çalıştı")
            }
        }


    }


        fun getLoadCryptoApi() {
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
            loading.value = true
            error.value = false
            // val results = mutableListOf<List<CryptoModel>>()
            val ids = cryptoies.joinToString(",")
            /*
listesindeki tüm ID'leri virgülle ayırarak tek bir String haline getiriyoruz.
Bu, API'ye tek seferde birden fazla kripto para sorgusu gönderebilmek için kullanılıyor.
 */

            launch(Dispatchers.IO) {
                    val  response = repository.getCrypto("usd", ids)


                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            response.body()?.let { response ->
                                println("response.isSuccessful")
                                for (crypto in response) {
                                    println(crypto.symbol.uppercase())  // `symbol` büyük harfe çevirip yazdırıyoruz
                                    println(crypto.currentPrice)        // Fiyat bilgisi
                                    println(crypto.image)

                                // Resim URL'si
                                }
                                /* response.forEach { crypto ->
                                  println(crypto.symbol.uppercase())  // Büyük harfe çevirerek yazdırıyoruz
                                  println(crypto.currentPrice)
                                  println(crypto.image)
                              }*/
                                showCryptos(response)
                              //  storeinSqlLite(response)


                              //  cryptoList.value = response
                                //initialCrypto.value = response

                               // loading.value = false
                                //error.value = false

                            }

                        } else {
                            error.value = true
                            loading.value = false
                            println("response.isNotSuccessful")
                            println(response.code())
                        }

                    }


                }

        }

   /* fun storeinSqlLite(Crypolist:List<CryptoModel>){
        launch(Dispatchers.IO) {

           // cryptoDao.deleteAll()

            cryptoDao.insertAll(*Crypolist.toTypedArray())// ile listeyi tekil elemanlara ayırıyoruz

            withContext(Dispatchers.Main) {
            sharedPreferences.saveApiCallTime(System.nanoTime())//zamanı kaydediyoruz

            }
        }

    }*/


  /*  fun getFromSqlLite(){
        loading.value=true
        launch(Dispatchers.IO) {
            val cryptoList = cryptoDao.getAllCrypto()
            withContext(Dispatchers.Main) {

                showCryptos(cryptoList)

            }

        }

    }*/
    fun showCryptos(Crypolist:List<CryptoModel>){

        cryptoList.value = Crypolist
        loading.value = false

        initialCrypto.value = Crypolist

        error.value = false
    }


    }

