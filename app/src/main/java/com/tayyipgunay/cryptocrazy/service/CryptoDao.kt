package com.tayyipgunay.cryptocrazy.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tayyipgunay.cryptocrazy.model.CryptoModel

@Dao
interface CryptoDao {
 //  @Insert
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(vararg cryptoModel: CryptoModel)

   @Query("SELECT * FROM CryptoModel")
   suspend fun getAllCrypto():List<CryptoModel>

    @Query("SELECT * FROM CryptoModel WHERE id = :id")
    suspend fun getCrypto(id: String): CryptoModel

    @Query("DELETE FROM CryptoModel")
    suspend fun deleteAll()


}