package com.tayyipgunay.cryptocrazy.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit


    class CustomSharedPreferences private constructor(context: Context) {

        private val sharedPreferences: SharedPreferences = context.getSharedPreferences("time_sharedpreferences", Context.MODE_PRIVATE)
        private val PREFERENCES_TIME = "last_api_call_time"
        //  val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

        fun saveApiCallTime(time: Long) {//
            sharedPreferences.edit(commit = true) {//
                putLong(PREFERENCES_TIME, time)//
            }
        }

        fun getTime(): Long {
            return sharedPreferences.getLong(PREFERENCES_TIME, 0)
        }

        companion object {
            @Volatile
            private var instance: CustomSharedPreferences? = null
            private val lock = Any()//
            // `invoke` operatörü ile sınıf fonksiyon gibi çağrılabilir
            operator fun invoke(context: Context): CustomSharedPreferences {
                return instance ?:
                synchronized(lock) {
                    instance ?:
                    CustomSharedPreferences(context).also {oluşanInstance->
                        instance = oluşanInstance
                    }
                }
            }
        }
    }
           /* operator fun invoke(context: Context): CustomSharedPrefences {//invoke operatörü, bir sınıf örneğinin fonksiyon gibi çağrılabilmesini sağlar.
                //     Bu, kodu daha sade ve kullanımı daha kolay hale getirir.
                // İlk kontrol: Eğer instance zaten oluşturulmuşsa, mevcut örneği döndür
                // Böylece synchronized bloğuna girmeye gerek kalmaz ve performans artar
                if (instance != null) {
                    return instance!!
                }

                // Eşzamanlılık kontrolü sağlamak için synchronized bloğu kullan
                // Aynı anda birden fazla iş parçacığının bu bloğu çalıştırmasını önler
                //A bekliyor mesela B oluşturusa a tekrar oluşturmasın diye ikinci null kontrolü yapıyoruz.
                synchronized(lock) {
                    //B içeride oluşturuyor
                    if (instance == null) {
                        instance =  CustomSharedPreferences(context)//
                    }
                    // Oluşturulan veya mevcut örneği döndür
                    return instance!!
                }
            }*/


