package com.ainsigne.interactivelivestreaming.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import java.util.stream.Stream

@Dao
interface StreamDetailsDao {

    @Query("SELECT * FROM chats WHERE channel = :channel ORDER BY dateCreated ")
    fun getStreamChatsFromChannel(channel : String): LiveData<List<StreamChat>>

    @Query("SELECT * FROM chats ORDER BY dateCreated ")
    fun getAllStreamChats(): LiveData<List<StreamChat>>

    @Query("SELECT * FROM streams WHERE channel = :channel ORDER BY dateCreated ")
    fun getStreamsFromChannel(channel : String): LiveData<Streams>

    @Query("SELECT * FROM users WHERE username = :userName ORDER BY dateCreated")
    fun getUser(userName : String): LiveData<StreamUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sendChat(streamChat : StreamChat)

}