package com.ainsigne.interactivelivestreaming.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class StreamUser(@PrimaryKey @ColumnInfo(name = "username") val userName : String,
                      val dateCreated : String) {
}