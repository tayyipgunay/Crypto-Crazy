package com.tayyipgunay.cryptocrazy.service

import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
//const val API_KEY = "A2DFFCC3-8106-4085-9E61-613194817020"
interface CryptoAPI {

    // Kripto para verilerini almak için API çağrısı
    @GET(Constants.EXCHANGE_RATE_ENDPOINT)
    suspend fun getCrypto(
        @Query("vs_currency") vsCurrency: String, // Karşılaştırılacak para birimi (örneğin USD, EUR)
        @Query("ids") ids: String, // Kripto paraların ID'leri (örneğin bitcoin, ethereum)
        @Query("x_cg_demo_api_key") apiKey: String // API anahtarı
    ): Response<List<CryptoModel>> // API'den gelen kripto para verilerini döndürür

    // Kripto para detaylarını almak için API çağrısı
    @GET(Constants.EXCHANGE_RATE_ENDPOINT)
    suspend fun getDetailCrypto(
        @Query("vs_currency") vsCurrency: String, // Karşılaştırılacak para birimi
        @Query("ids") ids: String, // Kripto paraların ID'leri
        @Query("x_cg_demo_api_key") apiKey: String // API anahtarı
    ): Response<List<CryptoModel>> // API'den gelen detaylı kripto para verilerini döndürür

}



