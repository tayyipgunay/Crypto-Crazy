package com.tayyipgunay.cryptocrazy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CryptoModel(
    @PrimaryKey
    @SerializedName("id")
    val id: String, // Kripto paranın benzersiz ID'si, Room veritabanında primary key olarak kullanılır.

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    val symbol: String, // Kripto para sembolü, örneğin BTC, ETH gibi.

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val image: String, // Kripto paranın logosunun URL'si.

    @ColumnInfo(name = "current_price")
    @SerializedName("current_price")
    val currentPrice: Double // Kripto paranın o anki fiyatı.
)

  /*  @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
*/
    //@ColumnInfo(name = "id")




