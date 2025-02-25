package com.tayyipgunay.cryptocrazy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CryptoModel(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    val symbol: String,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val image: String,

    @ColumnInfo(name = "current_price")
    @SerializedName("current_price")
    val currentPrice: Double
)
  /*  @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
*/
    //@ColumnInfo(name = "id")




