package com.ainsigne.interactivelivestreaming.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams

@Dao
interface StreamItemsDao {

    @Query("SELECT * FROM streams ORDER BY dateCreated DESC")
    fun getStreams(): LiveData<List<Streams>>

    @Query("SELECT * FROM users WHERE username = :userName ORDER BY dateCreated")
    fun getUser(userName : String): LiveData<StreamUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createStream(stream : Streams)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createAllStreams(streams : List<Streams>)
}