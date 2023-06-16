package com.example.callcenter.utils

import android.app.Application
import androidx.room.Room
import com.example.callcenter.entities.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun db(app: Application): AppDb {
        return Room.databaseBuilder(
            app, AppDb::class.java, "database-name"
        ).build()
    }

    @Provides
    @Singleton
    fun notificationManager(app: Application): AppNotificationManager {
        return AppNotificationManager(app)
    }

}

@HiltAndroidApp
class CallApp : Application() {}