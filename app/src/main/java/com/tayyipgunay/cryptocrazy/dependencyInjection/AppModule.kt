package com.tayyipgunay.cryptocrazy.dependencyInjection

import com.tayyipgunay.cryptocrazy.repository.CryptoRepository
import com.tayyipgunay.cryptocrazy.service.CryptoAPI
import com.tayyipgunay.cryptocrazy.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Hilt modülü tanımlıyoruz. Bu modül, bağımlılıkları nasıl sağlayacağımızı belirler.
// @Module: Bu sınıfın bir Hilt modülü olduğunu belirtir, Hilt bu sınıfta tanımlanan bağımlılıkları sağlar.
@Module
// Bu modülün SingletonComponent içinde çalışacağını belirtiriz.
// @InstallIn(SingletonComponent::class): Bu modülün uygulama boyunca tek bir örnekle (singleton) kullanılacağını ifade eder.
@InstallIn(SingletonComponent::class)
object AppModule {

    // Retrofit ile oluşturulacak `CryptoAPI` nesnesini singleton olarak sağlarız.
    // @Singleton: `CryptoAPI`'nin uygulama boyunca tek bir örneğinin oluşturulmasını sağlar.
    @Singleton
    @Provides
    /*
    @Provides fonksiyonu ile Hilt’e CryptoAPI nesnesinin nasıl oluşturulacağını gösteririz.
    Hilt, bu fonksiyonu kullanarak CryptoAPI örneğini (Retrofit'in sağladığı proxy sınıfını) oluşturur.
     */
    fun provideCryptoApi(): CryptoAPI {
        // Retrofit.Builder ile `CryptoAPI`'yi oluşturuyoruz.
        return Retrofit.Builder()//interface uygulamak diyelim
            .addConverterFactory(GsonConverterFactory.create()) // JSON verisini otomatik olarak Kotlin nesnelerine dönüştürmek için Gson kullanıyoruz.
            .baseUrl(BASE_URL) // API'nin temel (base) URL'sini ayarlıyoruz.
            .build() // Retrofit yapılandırmasını tamamlıyoruz.
            .create(CryptoAPI::class.java) // `CryptoAPI` arayüzü için bir istemci (client) oluşturuyoruz.
        // Bu istemci, API'den verileri almak için kullanılır.
        //ryptoAPI’nin Retrofit proxy nesnesi olarak tanımlanabilir.
        //
    }



    @Singleton
    @Provides
    fun provideCryptoRepository(api: CryptoAPI): CryptoRepository {
        return CryptoRepository(api)
    }




}

