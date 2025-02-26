package com.tayyipgunay.cryptocrazy.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tayyipgunay.cryptocrazy.model.CryptoModel

@Dao
interface CryptoDao {

    // Kripto para modelini veritabanına ekler (vararg parametre ile birden fazla öğe eklenebilir)
    @Insert(onConflict = OnConflictStrategy.REPLACE)//onConflict = OnConflictStrategy.REPLACE:
    // Bu parametre, veritabanına eklemeye çalışırken zaten mevcut olan verilerle karşılaşıldığında ne yapılacağını belirler. REPLACE stratejisi, zaten mevcut olan verileri (yani aynı birincil anahtara sahip olanları)
    // siler ve yenilerini ekler. Başka bir deyişle, var olan veri yerine yenisini koyar.
    suspend fun insertAll(vararg cryptoModel: CryptoModel)

    // Tüm kripto para verilerini alır
    @Query("SELECT * FROM CryptoModel")
    suspend fun getAllCrypto(): List<CryptoModel>

    // Belirli bir kripto parayı ID'sine göre alır
    @Query("SELECT * FROM CryptoModel WHERE id = :id")
    suspend fun getCrypto(id: String): CryptoModel

    // Tüm kripto para verilerini siler
    @Query("DELETE FROM CryptoModel")
    suspend fun deleteAll()
}
