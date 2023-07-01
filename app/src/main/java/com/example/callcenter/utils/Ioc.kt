package com.example.callcenter.utils

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.callcenter.entities.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.util.concurrent.Executors
import javax.inject.Singleton

class QueryLogger : RoomDatabase.QueryCallback {
    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        Log.d("quqquququuquququrqqqqqqqqqq", sqlQuery)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun db(app: Application): AppDb {
        return Room.databaseBuilder(
            app, AppDb::class.java, "database-name"
        ).setQueryCallback(
            QueryLogger(),
            Executors.newSingleThreadExecutor()
        ).build()
    }

    @Provides
    @Singleton
    fun notificationManager(app: Application): AppNotificationManager {
        return AppNotificationManager(app)
    }

    @Provides
    @Singleton
    fun phoneUtils(app: Application): PhoneNumberUtil {
        return PhoneNumberUtil.createInstance(app)
    }
}

@HiltAndroidApp
class CallApp : Application() {}