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
interface OnboardingDao {

    @Query("SELECT * FROM users WHERE username = :userName ORDER BY dateCreated")
    fun getUser(userName : String): LiveData<StreamUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(streamUser: StreamUser)


}