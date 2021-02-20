package com.ainsigne.interactivelivestreaming.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class StreamChat(@PrimaryKey @ColumnInfo(name = "id") val id : String, val chatMessage : String, val channel : String,
                      val user : String, val dateCreated : String) {
}