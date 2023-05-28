package com.example.callcenter.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.callcenter.entities.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object Ioc {
    fun db(context: Context): AppDb {
        return Room.databaseBuilder(
            context,
            AppDb::class.java, "database-name"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun db(app: Application): AppDb {
        return Room.databaseBuilder(
            app,
            AppDb::class.java, "database-name"
        ).build()
    }
}

@HiltAndroidApp
class CallApp : Application() {}