package com.tayyipgunay.cryptocrazy.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tayyipgunay.cryptocrazy.model.CryptoModel


    @Database(entities = arrayOf(CryptoModel::class), version = 1)
    abstract class CryptoDataBase : RoomDatabase() {

        abstract fun cryptoDao(): CryptoDao

        companion object {

            @Volatile
            private var instance: CryptoDataBase? = null
            // @Volatile: Bu değişken, çoklu iş parçacıklarının (thread) aynı anda bu değişkene erişiminde
            // her zaman güncel değeri görmesini sağlar. Yani, bir iş parçacığı bu değişkeni güncellediğinde,
            // diğer iş parçacıkları da hemen aynı güncel değeri görür.

            private val lock = Any()
            // `lock`: Çoklu iş parçacıkları aynı anda instance oluşturmak isterse,
            // birbirlerini beklemeleri için kullanılan bir mekanizma.

            /**
             * `operator fun invoke`: Bu, sınıfın bir nesnesi oluşturulurken çağrılan özel bir fonksiyondur.
             * Örneğin, `CryptoDataBase(context)` gibi bir çağrı yapıldığında `invoke` fonksiyonu tetiklenir.
             * Burada, veritabanı örneğini (instance) güvenli ve verimli bir şekilde oluşturmak için kullanıyoruz.
             */
            operator fun invoke(context: Context): CryptoDataBase {

                // Eğer instance zaten oluşturulmuşsa, mevcut örneği döndür (performans açısından hızlıdır).
                if (instance != null) {
                    return instance!!
                }

                // Eğer instance null ise, synchronized bloğuna gireriz.
                // synchronized: Aynı anda birden fazla iş parçacığının bu kodu çalıştırmasını önler.
                synchronized(lock) {
                    // Tekrar kontrol: Eğer hala null ise, veritabanı örneği oluşturulur.
                    if (instance == null) {
                        instance = makeDatabase(context) // Veritabanı nesnesi oluşturulur.
                    }
                    return instance!! // Artık oluşturulmuş olan instance döndürülür.
                }
            }

            /**
             * `makeDatabase`: Room kütüphanesi kullanılarak veritabanı nesnesini (instance) oluşturur.
             * `databaseBuilder`: Room’un sağladığı bir fonksiyon. Veritabanını tanımlamak için kullanılır.
             * Burada `CryptoDataBase` sınıfını veritabanı olarak belirtiyor ve ona bir isim veriyoruz ("cryptodatabase").
             */
            private fun makeDatabase(context: Context) =
                Room.databaseBuilder(
                    context.applicationContext, // Uygulamanın bağlamını (context) kullanıyoruz.
                    CryptoDataBase::class.java, // Veritabanını temsil eden sınıf.
                    "cryptodatabase"            // Veritabanının cihazda saklanacağı isim.
                ).build() // Veritabanını inşa et ve döndür.
        }
    }

