package com.tayyipgunay.cryptocrazy.service

import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
//const val API_KEY = "A2DFFCC3-8106-4085-9E61-613194817020"
interface CryptoAPI {



    @GET(Constants.EXCHANGE_RATE_ENDPOINT)
    suspend fun getCrypto(
        @Query("vs_currency") vsCurrency: String,
        @Query("ids") ids: String,
        @Query("x_cg_demo_api_key") apiKey: String
    ): Response<List<CryptoModel>>


    @GET(Constants.EXCHANGE_RATE_ENDPOINT)
    suspend fun getDetailCrypto(
        @Query("vs_currency") vsCurrency: String,
        @Query("ids") ids: String,
        @Query("x_cg_demo_api_key") apiKey: String

    ): Response<List<CryptoModel>>



}


