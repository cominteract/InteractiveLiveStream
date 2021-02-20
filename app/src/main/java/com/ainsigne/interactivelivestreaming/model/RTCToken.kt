package com.ainsigne.interactivelivestreaming.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rtctokens")
data class RTCToken(@PrimaryKey @ColumnInfo(name = "account") val account : String, val channel : String) {
}