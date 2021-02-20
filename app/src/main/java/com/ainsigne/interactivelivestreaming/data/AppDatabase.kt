package com.ainsigne.interactivelivestreaming.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ainsigne.interactivelivestreaming.model.RTCToken
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams

/**
 * The Room database for this app
 */
@Database(entities = [StreamChat::class, StreamUser::class, Streams::class, RTCToken::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * the dao for room access to perform crud on [Streams]
     * @return [StreamItemsDao]
     */
    abstract val streamItemsDao : StreamItemsDao
    /**
     * the dao for room access to perform crud on [Streams]
     * @return [StreamDetailsDao]
     */

    abstract val streamDetailsDao : StreamDetailsDao

    /**
     * the dao for room access to perform crud on [StreamUser]
     * @return [OnboardingDao]
     */

    abstract val onboardingDao : OnboardingDao
}
