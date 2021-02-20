package com.ainsigne.interactivelivestreaming.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ainsigne.interactivelivestreaming.data.AppDatabase
import com.ainsigne.interactivelivestreaming.data.OnboardingDao
import com.ainsigne.interactivelivestreaming.data.StreamDetailsDao
import com.ainsigne.interactivelivestreaming.data.StreamItemsDao
import com.ainsigne.interactivelivestreaming.utils.DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    single { createDatabase(androidContext()) }
    single { createStreamItemsDao(get()) }
    single { createStreamDetailsDao(get()) }
    single { createOnboardingDao(get()) }
}

private fun createDatabase(appContext: Context) : AppDatabase{
    return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        })
        .build()
}

private fun createStreamItemsDao(database: AppDatabase) : StreamItemsDao{
    return database.streamItemsDao
}

private fun createStreamDetailsDao(database: AppDatabase) : StreamDetailsDao{
    return database.streamDetailsDao
}

private fun createOnboardingDao(database: AppDatabase) : OnboardingDao{
    return database.onboardingDao
}