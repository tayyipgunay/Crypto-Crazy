package com.tayyipgunay.cryptocrazy.repository

import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.service.CryptoAPI
import com.tayyipgunay.cryptocrazy.util.Constants
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


// Bu sınıf, kripto verilerini almak için bir repository (depo) işlevi görür.
// Hilt tarafından sağlanan bağımlılık enjeksiyonuyla oluşturulur.
/*@Singleton
class CryptoRepository @Inject constructor(private val api: CryptoAPI) {



suspend fun getCrypto(vsCurrency: String, ids: String): Response<List<CryptoModel>> {
    val response = api.getCrypto(vsCurrency, ids, Constants.API_KEY)// API'ye istek gönderilir ve yanıt alınır

    return if (response.isSuccessful) {
        response
    } else {
        response
    }


}*/


// Kripto para verilerini almak için kullanılan repository sınıfı.
// @Singleton ile tek bir örnek olarak tanımlandı, @Inject ile Hilt tarafından `CryptoAPI` enjekte edilir.



@Singleton
class CryptoRepository @Inject constructor(private val api: CryptoAPI) {//
//inject sayesinde api tekrar tanımlamam gerek kalmadı

    // API'den kripto para verilerini almak için asenkron fonksiyon
    suspend fun getCrypto(vsCurrency: String, ids: String): Response<List<CryptoModel>> {
        // API'ye istek gönderir ve yanıtı döndürür.
        // Yanıt başarılı veya başarısız olsa da doğrudan yanıt döner.
        return api.getCrypto(vsCurrency, ids, Constants.API_KEY)
        //response döner yani
    }
    suspend fun getDetailCrypto(vsCurrency: String, ids: String): Response<List<CryptoModel>> {
        return api.getDetailCrypto(vsCurrency, ids, Constants.API_KEY)
    }
}


